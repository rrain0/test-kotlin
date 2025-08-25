package `event-engine`.`live-map`

import com.rrain.util.base.`date-time`.now
import com.rrain.util.base.number.ifZero
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import java.util.TreeSet
import java.util.UUID
import kotlin.compareTo
import kotlin.time.Duration.Companion.minutes




typealias UserId = UUID
typealias SessionId = UUID

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
  fun toSessionDataInternal(accessedAt: Instant) = SessionDataInternal(
    id = id,
    accessedAt = now(),
    expiresAt = expiresAt,
    userId = userId,
    onlineAt = onlineAt,
    online = online,
  )
}





data class SessionDataInternal(
  val id: SessionId,
  val accessedAt: Instant?,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
  val online: Boolean = false,
) {
  val staleAt = minOf(accessedAt?.let { it + 3.minutes } ?: expiresAt, expiresAt)
  
  companion object {
    val stalenessToIdComparator = Comparator<SessionDataInternal> { a, b ->
      a.staleAt compareTo b.staleAt ifZero { a.id compareTo b.id }
    }
  }
  
  fun toSessionData() = SessionData(
    id = id,
    expiresAt = expiresAt,
    userId = userId,
    onlineAt = onlineAt,
    online = online,
  )
}


object LiveSession {
  private val data: MutableMap<SessionId, SessionDataInternal> = mutableMapOf()
  
  suspend fun get(id: SessionId): SessionData? {
    val (curr0, next0) = synchronized(this) {
      val curr0 = data[id]
      val next0 = curr0?.let { curr0 ->
        val next0 = curr0.copy(accessedAt = now())
        data[id] = next0
        next0
      }
      curr0 to next0
    }
    if (curr0 == null && next0 == null) {
      coroutineScope { launch {
        StoredSession.get(id)
          ?.toSessionData(online = false)
          ?.let { stored -> add(stored) }
      } }
    }
    if (curr0 != null && next0 != null) {
      val cachedPrev = curr0
      val cachedNext = next0
      // TODO Push access event to cache
    }
    
    val next = next0?.toSessionData()
    return next
  }
  
  // add if not exists by id
  suspend fun add(upd: SessionData) {
    val (curr0, next0) = synchronized(this) {
      val curr0 = data[upd.id]
      if (curr0 != null) return
      val next0 = upd.toSessionDataInternal(accessedAt = now())
      data[upd.id] = next0
      curr0 to next0
    }
    
    val cachedPrev = curr0
    val cachedNext = next0
    // TODO Push access event to cache
    
    val curr = upd.copy(onlineAt = null, online = false)
    val next = upd
    
    SessionOnlineInner.tryEmit(curr, next)
  }
  
  suspend fun addOrUpdate(upd: SessionData) {
    val (curr0, next0) = synchronized(this) {
      val curr0 = data[upd.id]
      val next0 = upd.toSessionDataInternal(accessedAt = now())
        .copy(onlineAt = upd.onlineAt ?: curr0?.onlineAt)
      data[upd.id] = next0
      curr0 to next0
    }
    
    val cachedPrev = curr0
    val cachedNext = next0
    // TODO Push access event to cache
    
    val curr = curr0?.toSessionData()
      ?: next0.toSessionData().copy(onlineAt = null, online = false)
    val next = next0.toSessionData()
    
    SessionOnlineInner.tryEmit(curr, next)
  }
  
  suspend fun remove(id: SessionId) {
    val curr0 = synchronized(this) { data.remove(id) } ?: return
    val next0: SessionDataInternal? = null
    
    val cachedPrev = curr0
    val cachedNext = next0
    // TODO Push access event to cache
    
    val curr = curr0.toSessionData()
    val next = curr.copy(online = false)
    
    coroutineScope { launch {
      StoredSession.addOrUpdate(next.toStoredSessionData())
    } }
    SessionOnlineInner.tryEmit(curr, next)
  }
}





interface SessionEv
data class SessionOnlineEv(val data: SessionData) : SessionEv

private object SessionOnlineInner {
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
    
    if (onlineChange) {
      flow.emit(SessionOnlineEv(next))
    }
  }
}

object SessionOnline {
  val events by SessionOnlineInner::events
}



// TODO
object SessionUsage {
  private val sortedSet = TreeSet(SessionDataInternal.stalenessToIdComparator)
  
  fun use(curr: SessionDataInternal?, next: SessionDataInternal?) {
  
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

object StoredSession {
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
