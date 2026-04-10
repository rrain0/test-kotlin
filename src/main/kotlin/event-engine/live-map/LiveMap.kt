package `event-engine`.`live-map`

import com.rrain.utils.base.`date-time`.isExpired
import com.rrain.utils.base.`date-time`.now
import com.rrain.utils.base.number.ifZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
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
  LiveSessions.runUnusedCleaner()
  SessionsUsage.runCleaner()
}





data class SessionData(
  val id: SessionId,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
  val online: Boolean = false,
) {
  val unused get() = expiresAt.isExpired() || !online
  fun toStoredSessionData() = StoredSessionData(
    id = id,
    expiresAt = expiresAt,
    userId = userId,
    onlineAt = onlineAt,
  )
}

private object LiveSessions {
  private val data = mutableMapOf<SessionId, SessionData>()
  
  suspend fun get(id: SessionId): SessionData? {
    val curr = data[id]
    
    if (curr == null) {
      config.bgScope.launch {
        StoredSessions.get(id)
          ?.toSessionData(online = false)
          ?.let { stored -> add(stored) }
      }
    }
    
    if (curr != null) {
      SessionsUsage.addOrUpdate(curr)
    }
    
    return curr
  }
  
  // add if not exists by id
  suspend fun add(upd: SessionData) {
    synchronized(this) {
      if (upd.id in data) return
      data[upd.id] = upd
    }
    
    val curr = upd.copy(onlineAt = null, online = false)
    val next = upd
    
    SessionsUsage.addOrUpdate(next)
    withContext(NonCancellable) {
      OnlineSessions.tryEmitEvent(curr, next)
    }
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
    
    SessionsUsage.addOrUpdate(next)
    withContext(NonCancellable) {
      OnlineSessions.tryEmitEvent(curr, next)
    }
  }
  
  suspend fun remove(id: SessionId, removeOnlyUnused: Boolean = false) {
    val curr = synchronized(this) { data.remove(id) } ?: return
    if (removeOnlyUnused && !curr.unused) return
    val next = curr.copy(online = false)
    
    SessionsUsage.remove(next.id)
    
    config.bgScope.launch {
      StoredSessions.addOrUpdate(next.toStoredSessionData())
    }
    
    withContext(NonCancellable) {
      OnlineSessions.tryEmitEvent(curr, next)
    }
  }
  
  fun runUnusedCleaner() {
    config.bgScope.launch {
      while (isActive) SessionsUsage.events.collect {
        if (it.type === CacheEvType.UNUSED) {
          val id = it.data.id
          remove(id, removeOnlyUnused = true)
        }
      }
    }
  }
}

object LiveSessionsShared {
  suspend fun get(id: SessionId) = LiveSessions.get(id)
  suspend fun add(upd: SessionData) = LiveSessions.add(upd)
  suspend fun addOrUpdate(upd: SessionData) = LiveSessions.addOrUpdate(upd)
  suspend fun remove(id: SessionId) = LiveSessions.remove(id)
}





data class SessionOnlineEv(val data: SessionData)

private object OnlineSessions {
  private val flow = MutableSharedFlow<SessionOnlineEv>(extraBufferCapacity = 1)
  val events = flow.asSharedFlow()
  
  suspend fun tryEmitEvent(curr: SessionData, next: SessionData) {
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





enum class CacheEvType { ADD, UPDATE, REMOVE, UNUSED }
data class CacheEv(val type: CacheEvType, val data: SessionUsageData)

data class SessionUsageData(
  val id: SessionId,
  val accessedAt: Instant,
  val expiresAt: Instant,
  val online: Boolean,
) : Comparable<SessionUsageData> {
  val unusedAt get() = when {
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

private object SessionsUsage {
  private val map = mutableMapOf<SessionId, SessionUsageData>()
  private val sorted = TreeSet<SessionUsageData>()
  
  private val updates = MutableSharedFlow<CacheEv>(extraBufferCapacity = 1)
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
      val curr = map.remove(id) ?: return
      sorted.remove(curr)
      ev = CacheEv(CacheEvType.REMOVE, curr)
    }
    withContext(NonCancellable) {
      if (ev != null) updates.emit(ev)
    }
  }
  
  
  
  fun runCleaner() {
    config.bgScope.launch {
      while (isActive) {
        val runWaiterCleaner = suspend { launch {
          while (isActive) {
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
               if (item != null) updates.emit(CacheEv(CacheEvType.UNUSED, item))
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

object SessionsUsageShared {
  val events by SessionsUsage::events
}





// Сессии онлайн хранятся в оперативке.
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
  private val storedData = mutableMapOf<SessionId, StoredSessionData>()
  
  suspend fun get(id: SessionId): StoredSessionData? {
    delay(1234) // emulate async delay
    val curr = synchronized(this) { storedData[id] }
    return curr
  }
  
  suspend fun addOrUpdate(upd: StoredSessionData) {
    delay(1234) // emulate async delay
    synchronized(this) {
      val curr = storedData[upd.id]
      val next = upd.copy(onlineAt = upd.onlineAt ?: curr?.onlineAt)
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
