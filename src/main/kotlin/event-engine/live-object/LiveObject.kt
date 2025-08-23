package `event-engine`.`live-object`

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import java.util.UUID




typealias UserId = UUID
typealias SessionId = UUID

data class SessionData(
  val id: SessionId,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
  val online: Boolean = false,
)

// Сессии онлайн, хранятся в оперативке.
// Сессии оффлайн будут в БД. Или не будут, если о них ещё нет данных
data class StoredSessionData(
  val id: SessionId,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
)

interface SessionEv
data class SessionOnlineEv(val data: SessionData) : SessionEv




object LiveSession {
  // No need volatile because access is always synchronized.
  /*
  The synchronized keyword provides both atomicity and visibility guarantees.
  When a thread enters a synchronized method, it acquires a lock on the object.
  This ensures that only one thread can execute that method
  (or any other synchronized method on the same object) at a time,
  guaranteeing atomicity for operations within the method. Crucially,
  when a thread exits a synchronized block or method, it performs a "write barrier",
  which flushes all changes made by that thread to main memory,
  making them visible to other threads. Conversely,
  when a thread enters a synchronized block or method, it performs a "read barrier",
  which invalidates its local cache and forces it to read the latest values from main memory.
   */
  private var data: SessionData? = null
  
  suspend fun get(id: SessionId): SessionData? {
    val curr = synchronized(this) {
      data?.takeIf { it.id == id }
    }
    if (curr == null) {
      coroutineScope { launch {
        val stored = StoredSession.get(id)?.let { SessionData(
          id = it.id,
          expiresAt = it.expiresAt,
          userId = it.userId,
          onlineAt = it.onlineAt,
          online = false,
        ) }
        if (stored != null) add(stored)
      } }
    }
    return curr
  }
  
  suspend fun add(upd: SessionData) {
    val (curr, next) = synchronized(this) {
      val curr = data
      val next = upd
      if (curr != null) return
      run {
        data = next
        val curr = SessionData(
          id = upd.id,
          expiresAt = upd.expiresAt,
          userId = upd.userId,
          onlineAt = null,
          online = false,
        )
        curr to next
      }
    }
    
    emitSessionUpdate(curr, next)
  }
  
  suspend fun addOrUpdate(upd: SessionData) {
    val (curr, next) = synchronized(this) {
      val curr = data?.takeIf { it.id == upd.id } ?: SessionData(
        id = upd.id,
        expiresAt = upd.expiresAt,
        userId = upd.userId,
        onlineAt = null,
        online = false,
      )
      val next = upd.let {
        it.copy(onlineAt = it.onlineAt ?: curr.onlineAt)
      }
      data = next
      curr to next
    }
    
    emitSessionUpdate(curr, next)
  }
  
  suspend fun remove(id: SessionId) {
    val (curr, next) = synchronized(this) {
      val curr = data?.takeIf { it.id == id } ?: return
      data = null
      val next = SessionData(
        id = curr.id,
        expiresAt = curr.expiresAt,
        userId = curr.userId,
        onlineAt = curr.onlineAt,
        online = false,
      )
      curr to next
    }
    
    coroutineScope { launch {
      StoredSession.addOrUpdate(StoredSessionData(
        id = next.id,
        expiresAt = next.expiresAt,
        userId = next.userId,
        onlineAt = next.onlineAt,
      ))
    } }
    emitSessionUpdate(curr, next)
  }
}



// Flow to push updates
private val sessionOnlineUpdateFlow = MutableSharedFlow<SessionOnlineEv>()
val sessionOnlineUpdateEvents = sessionOnlineUpdateFlow.asSharedFlow()

private suspend fun emitSessionUpdate(curr: SessionData, next: SessionData) {
  var onlineChange = false
  
  // Изменился сам статус онлайн.
  if (curr.online != next.online) {
    onlineChange = true
  }
  // Если статус онлайн не изменился:
  // Если мы остались онлайн, то изменение onlineAt не имеет значения, потому что в UI мы показываем online.
  // Если мы остались оффлайн, то изменение onlineAt имеет значение и именно его мы показываем в UI.
  else if (!next.online && curr.onlineAt != next.onlineAt) {
    onlineChange = true
  }
  
  if (onlineChange) {
    sessionOnlineUpdateFlow.emit(SessionOnlineEv(next))
  }
}





object StoredSession {
  private var storedData: StoredSessionData? = null
  
  suspend fun get(id: SessionId): StoredSessionData? {
    delay(1234)
    synchronized(this) {
      return storedData?.takeIf { it.id == id }
    }
  }
  
  suspend fun addOrUpdate(upd: StoredSessionData) {
    delay(1234)
    synchronized(this) {
      val curr = storedData?.takeIf { it.id == upd.id } ?: StoredSessionData(
        id = upd.id,
        expiresAt = upd.expiresAt,
        userId = upd.userId,
        onlineAt = null,
      )
      val next = upd.let {
        it.copy(onlineAt = it.onlineAt ?: curr.onlineAt)
      }
      storedData = next
    }
  }
  
  suspend fun remove(id: SessionId) {
    delay(1234)
    synchronized(this) {
      storedData?.takeIf { it.id == id } ?: return
      storedData = null
    }
  }
}
