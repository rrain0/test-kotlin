package `event-engine`.`live-object`

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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




interface SessionEv
data class SessionOnlineEv(val data: SessionData) : SessionEv


// Flow to push updates
private val sessionOnlineUpdateFlow = MutableSharedFlow<SessionOnlineEv>()
val sessionOnlineUpdateEvents = sessionOnlineUpdateFlow.asSharedFlow()




private var sessionData: SessionData? = null




fun getSession(id: SessionId): SessionData? = sessionData?.takeIf { it.id == id }

suspend fun addOrUpdateSession(upd: SessionData) {
  val curr = sessionData?.takeIf { it.id == upd.id } ?: SessionData(
    id = upd.id,
    expiresAt = upd.expiresAt,
    userId = upd.userId,
    onlineAt = null,
    online = false,
  )
  val next = upd.let { it.copy(onlineAt = if (it.online) it.onlineAt else curr.onlineAt) }
  sessionData = next
  
  emitSessionUpdate(curr, next)
}

suspend fun removeSession(id: SessionId) {
  val curr = sessionData?.takeIf { it.id == id } ?: return
  val next = SessionData(
    id = curr.id,
    expiresAt = curr.expiresAt,
    userId = curr.userId,
    onlineAt = null,
    online = false,
  )
  sessionData = null
  
  emitSessionUpdate(curr, next)
}


private suspend fun emitSessionUpdate(curr: SessionData, next: SessionData) {
  var onlineChange = false
  
  if (curr.online != next.online) {
    onlineChange = true
  }
  else if (!next.online && curr.onlineAt != next.onlineAt) {
    onlineChange = true
  }
  
  if (onlineChange) {
    sessionOnlineUpdateFlow.emit(SessionOnlineEv(next))
  }
}