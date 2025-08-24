package `event-engine`.`live-map`

import com.google.common.collect.TreeMultimap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import java.util.UUID
import kotlin.compareTo
import kotlin.time.Duration.Companion.minutes




typealias UserId = UUID
typealias SessionId = UUID





data class SessionData(
  val id: SessionId,
  //val accessedAt: Instant,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
  val online: Boolean = false,
)

object LiveSession {
  private val data: MutableMap<SessionId, SessionData> = mutableMapOf()
  
  suspend fun get(id: SessionId): SessionData? {
    val curr = synchronized(this) { data[id] }
    if (curr == null) {
      coroutineScope { launch {
        StoredSession.get(id)
          ?.let { SessionData(
            id = it.id,
            expiresAt = it.expiresAt,
            userId = it.userId,
            onlineAt = it.onlineAt,
            online = false,
          ) }
          ?.let { stored -> add(stored) }
      } }
    }
    if (curr != null) {
      // TODO Push access event to cache
    }
    return curr
  }
  
  // add if not exists by id
  suspend fun add(upd: SessionData) {
    val (curr, next) = synchronized(this) {
      val curr = data[upd.id]
      val next = upd
      curr ?: return
      run {
        data[upd.id] = next
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
    
    // TODO Push access event to cache
    
    SessionOnlineInner.tryEmit(curr, next)
  }
  
  suspend fun addOrUpdate(upd: SessionData) {
    val (curr, next) = synchronized(this) {
      val curr = data[upd.id] ?: SessionData(
        id = upd.id,
        expiresAt = upd.expiresAt,
        userId = upd.userId,
        onlineAt = null,
        online = false,
      )
      val next = upd.let {
        it.copy(onlineAt = it.onlineAt ?: curr.onlineAt)
      }
      data[upd.id] = next
      curr to next
    }
    
    SessionOnlineInner.tryEmit(curr, next)
  }
  
  suspend fun remove(id: SessionId) {
    val (curr, next) = synchronized(this) {
      val curr = data.remove(id) ?: return
      val next = SessionData(
        id = curr.id,
        expiresAt = curr.expiresAt,
        userId = curr.userId,
        onlineAt = curr.onlineAt,
        online = false,
      )
      curr to next
    }
    
    // TODO Push remove event to cache
    
    coroutineScope { launch {
      StoredSession.addOrUpdate(StoredSessionData(
        id = next.id,
        expiresAt = next.expiresAt,
        userId = next.userId,
        onlineAt = next.onlineAt,
      ))
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




data class SessionUsageData(
  val id: SessionId,
  val expiresAt: Instant,
  val accessedAt: Instant,
) {
  val staleAt = minOf(accessedAt + 3.minutes, expiresAt)
  companion object {
    val identityComparator = Comparator<SessionUsageData> { a, b -> a.id compareTo b.id }
    val stalenessComparator = Comparator<SessionUsageData> { a, b -> a.staleAt compareTo b.staleAt }
  }
}

// TODO
object SessionUsage {
  private val sortedMap = SessionUsageData.run {
    TreeMultimap.create(stalenessComparator, identityComparator)
  }
  
  
  fun use(curr: SessionUsageData?, next: SessionUsageData?) {
  
  }
}





// Сессии онлайн, хранятся в оперативке.
// Сессии оффлайн будут в БД. Или не будут, если о них ещё нет данных
data class StoredSessionData(
  val id: SessionId,
  val expiresAt: Instant,
  val userId: UserId? = null,
  val onlineAt: Instant? = null,
)

object StoredSession {
  private val storedData: MutableMap<SessionId, StoredSessionData> = mutableMapOf()
  
  suspend fun get(id: SessionId): StoredSessionData? {
    delay(1234) // emulate async delay
    return synchronized(this) { storedData[id] }
  }
  
  suspend fun addOrUpdate(upd: StoredSessionData) {
    delay(1234) // emulate async delay
    synchronized(this) {
      val curr = storedData[upd.id] ?: StoredSessionData(
        id = upd.id,
        expiresAt = upd.expiresAt,
        userId = upd.userId,
        onlineAt = null,
      )
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
