package `event-engine`.`live-map`

import com.rrain.util.base.`date-time`.now
import com.rrain.util.base.number.ifZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import java.util.TreeSet
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes




typealias UserId = UUID
typealias SessionId = UUID


data class SessionsConfig(
  var bgScope: CoroutineScope,
  val cacheLifetime: Duration,
)
private lateinit var config: SessionsConfig

fun initSessionStorage(
  bgScope: CoroutineScope,
  cacheLifetime: Duration = 3.minutes
) {
  config = SessionsConfig(
    bgScope = bgScope,
    cacheLifetime = cacheLifetime,
  )
  UnusedSessions.runCleaner()
}



data class SessionData(
  val id: SessionId,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
  val online: Boolean = false,
) {
  fun toStoredSessionData() = StoredSessionData(
    id = id,
    expiresAt = expiresAt,
    userId = userId,
    onlineAt = onlineAt,
  )
}





object LiveSessions {
  private val data: MutableMap<SessionId, SessionData> = mutableMapOf()
  
  suspend fun get(id: SessionId): SessionData? {
    val curr = data[id]
    
    if (curr == null) {
      config.bgScope.launch {
        StoredSessions.get(id)
          ?.toSessionData(online = false)
          ?.let { stored -> add(stored) }
      }
    }
    
    if (curr != null) UnusedSessions.addOrUpdate(curr)
    
    return curr
  }
  
  // add if not exists by id
  suspend fun add(upd: SessionData) {
    synchronized(this) {
      if (data.contains(upd.id)) return
      data[upd.id] = upd
    }
    
    val curr = upd.copy(onlineAt = null, online = false)
    val next = upd
    
    UnusedSessions.addOrUpdate(next)
    OnlineSessions.tryEmit(curr, next)
  }
  
  suspend fun addOrUpdate(upd: SessionData) {
    val (curr0, next0) = synchronized(this) {
      val curr = data[upd.id]
      val next = upd.copy(onlineAt = upd.onlineAt ?: curr?.onlineAt)
      data[upd.id] = next
      curr to next
    }
    
    val curr = curr0 ?: next0.copy(onlineAt = null, online = false)
    val next = next0
    
    UnusedSessions.addOrUpdate(next)
    OnlineSessions.tryEmit(curr, next)
  }
  
  suspend fun remove(id: SessionId) {
    val curr = synchronized(this) { data.remove(id) } ?: return
    val next = curr.copy(online = false)
    
    UnusedSessions.remove(next.id)
    
    config.bgScope.launch {
      StoredSessions.addOrUpdate(next.toStoredSessionData())
    }
    OnlineSessions.tryEmit(curr, next)
  }
}





interface SessionEv
data class SessionOnlineEv(val data: SessionData) : SessionEv

private object OnlineSessions {
  // Flow to push updates
  private val flow = MutableSharedFlow<SessionOnlineEv>()
  val events = flow.asSharedFlow()
  
  suspend fun tryEmit(curr: SessionData, next: SessionData) {
    val onlineChange = (
      // Изменился сам статус онлайн.
      (curr.online != next.online) ||
      // Если статус онлайн не изменился:
      // Если мы остались онлайн, то изменение onlineAt не имеет значения, потому что в UI мы показываем online.
      // Если мы остались оффлайн, то изменение onlineAt имеет значение потому что его мы показываем в UI.
      (!next.online && curr.onlineAt != next.onlineAt)
    )
    
    withContext(NonCancellable) {
      if (onlineChange) {
        flow.emit(SessionOnlineEv(next))
      }
    }
  }
}

object OnlineSessionsShared {
  val events by OnlineSessions::events
}




enum class CacheEvType { ADD, UPDATE, REMOVE, EXPIRED }
data class CacheEv(
  val type: CacheEvType,
  val data: SessionUsageData,
)

data class SessionUsageData(
  val id: SessionId,
  val accessedAt: Instant,
  val expiresAt: Instant,
  val online: Boolean,
) : Comparable<SessionUsageData> {
  val unusedAt = when {
    online -> expiresAt
    else -> minOf(expiresAt, accessedAt + config.cacheLifetime)
  }
  
  override fun compareTo(other: SessionUsageData) =
    unusedAt.compareTo(other.unusedAt)
      .ifZero { id.compareTo(other.id) }
}

fun SessionData.toSessionUsageData(accessedAt: Instant = now()) = SessionUsageData(
  id = id,
  accessedAt = accessedAt,
  expiresAt = expiresAt,
  online = online,
)

object UnusedSessions {
  private val map = mutableMapOf<SessionId, SessionUsageData>()
  private val sorted = TreeSet<SessionUsageData>()
  
  private val updates = MutableSharedFlow<CacheEv>()
  val events = updates.asSharedFlow()
  
  suspend fun addOrUpdate(upd: SessionData) {
    val next = upd.toSessionUsageData()
    var ev: CacheEv
    synchronized(this) {
      // Check if entry the same
      if (next in sorted) {
        ev = CacheEv(CacheEvType.UPDATE, next)
      }
      else {
        val curr = map.remove(next.id)
        // Adding new entry
        if (curr == null) {
          ev = CacheEv(CacheEvType.ADD, next)
          map[next.id] = next
          sorted += next
        }
        // Updating old entry
        else {
          sorted.remove(curr)
          ev = CacheEv(CacheEvType.UPDATE, next)
          map[next.id] = next
          sorted += next
        }
      }
    }
    withContext(NonCancellable) {
      updates.emit(ev)
    }
  }
  
  suspend fun remove(id: SessionId) {
    var ev: CacheEv? = null
    synchronized(this) {
      // Remove entry
      val curr = map.remove(id)
      curr ?: return
      sorted.remove(curr)
      ev = CacheEv(CacheEvType.REMOVE, curr)
    }
    withContext(NonCancellable) {
      if (ev != null) updates.emit(ev)
    }
  }
  
  
  
  fun runCleaner() {
    config.bgScope.launch {
      while (true) {
        val runWaiterCleaner = suspend { launch {
          while (true) {
            val firstUnusedAt = synchronized(this) { sorted.firstOrNull() }?.unusedAt
            firstUnusedAt ?: break
            
            delay(firstUnusedAt - now())
            
            val item = synchronized(this) {
              val item = sorted.firstOrNull()
              if (item != null && item.unusedAt <= now()) {
                map -= item.id
                sorted -= item
              }
              item
            }
            
            withContext(NonCancellable) {
               if (item != null) updates.emit(CacheEv(CacheEvType.EXPIRED, item))
            }
          }
        } }
        
        var waiterCleanerJob = runWaiterCleaner()
        updates.collect {
          waiterCleanerJob.cancel()
          waiterCleanerJob = runWaiterCleaner()
        }
      }
    }
  }
}





// Сессии онлайн, хранятся в оперативке.
// Сессии оффлайн будут в БД. Или не будут, если о них ещё нет данных
data class StoredSessionData(
  val id: SessionId,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
) {
  fun toSessionData(online: Boolean) = SessionData(
    id = id,
    expiresAt = expiresAt,
    userId = userId,
    onlineAt = onlineAt,
    online = online,
  )
}

object StoredSessions {
  private val storedData: MutableMap<SessionId, StoredSessionData> = mutableMapOf()
  
  suspend fun get(id: SessionId): StoredSessionData? {
    delay(1234) // emulate async delay
    return synchronized(this) { storedData[id] }
  }
  
  suspend fun addOrUpdate(upd: StoredSessionData) {
    delay(1234) // emulate async delay
    synchronized(this) {
      val curr = storedData[upd.id] ?: upd.copy(onlineAt = null)
      val next = upd.let {
        it.copy(onlineAt = it.onlineAt ?: curr.onlineAt)
      }
      storedData[upd.id] = next
    }
  }
  
  suspend fun remove(id: SessionId) {
    delay(1234) // emulate async delay
    synchronized(this) {
      storedData.remove(id)
    }
  }
}
