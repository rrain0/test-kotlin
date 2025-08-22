package `event-engine`.`live-object`

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.datetime.Instant
import java.util.UUID
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.concurrent.Volatile




typealias UserId = UUID
typealias SessionId = UUID




data class SessionData(
  val id: SessionId,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
  val online: Boolean = false,
)




interface SessionEv
data class SessionOnlineEv(val data: SessionData) : SessionEv


// Flow to push updates
private val sessionOnlineUpdateFlow = MutableSharedFlow<SessionOnlineEv>()
val sessionOnlineUpdateEvents = sessionOnlineUpdateFlow.asSharedFlow()



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
  private var sessionData: SessionData? = null
  
  fun getSession(id: SessionId): SessionData? = synchronized(this) {
    sessionData?.takeIf { it.id == id }
  }
  
  suspend fun addOrUpdateSession(upd: SessionData) {
    val (curr, next) = synchronized(this) {
      val curr = sessionData?.takeIf { it.id == upd.id } ?: SessionData(
        id = upd.id,
        expiresAt = upd.expiresAt,
        userId = upd.userId,
        onlineAt = null,
        online = false,
      )
      val next = upd.let { it.copy(onlineAt = if (it.online) it.onlineAt else curr.onlineAt) }
      sessionData = next
      curr to next
    }
    
    emitSessionUpdate(curr, next)
  }
  
  suspend fun removeSession(id: SessionId) {
    val (curr, next) = synchronized(this) {
      val curr = sessionData?.takeIf { it.id == id } ?: return
      val next = SessionData(
        id = curr.id,
        expiresAt = curr.expiresAt,
        userId = curr.userId,
        onlineAt = null,
        online = false,
      )
      sessionData = null
      curr to next
    }
    
    emitSessionUpdate(curr, next)
  }
}



private suspend fun emitSessionUpdate(curr: SessionData, next: SessionData) {
  var onlineChange = false
  
  if (curr.online != next.online) {
    onlineChange = true
  }
  else if (next.online && curr.onlineAt != next.onlineAt) {
    onlineChange = true
  }
  
  if (onlineChange) {
    sessionOnlineUpdateFlow.emit(SessionOnlineEv(next))
  }
}