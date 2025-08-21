package `event-engine`

import com.rrain.util.base.`date-time`.now
import com.rrain.util.base.uuid.randomUuid
import `event-engine`.`live-object`.SessionData
import `event-engine`.`live-object`.addOrUpdateSession
import `event-engine`.`live-object`.sessionOnlineUpdateEvents
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.days




fun main() {
  liveObject()
}


fun liveObject() = runBlocking(Executors.newCachedThreadPool().asCoroutineDispatcher()) {
  val id = randomUuid()
  val userId = randomUuid()
  val expiresAt = now() + 10.days
  
  launch {
    SessionData(id, expiresAt, userId, now(), true).let {
      println("addOrUpdateSession: $it")
      addOrUpdateSession(it)
    }
    
    delay(1000)
    SessionData(id, expiresAt, userId, now(), true).let {
      println("addOrUpdateSession: $it")
      addOrUpdateSession(it)
    }
    
    delay(2000)
    SessionData(id, expiresAt, userId, online = false).let {
      println("addOrUpdateSession: $it")
      addOrUpdateSession(it)
    }
    
    delay(1000)
    SessionData(id, expiresAt, userId, now(), true).let {
      println("addOrUpdateSession: $it")
      addOrUpdateSession(it)
    }
  }
  
  launch {
    delay(100)
    sessionOnlineUpdateEvents.collect {
      println("onSessionOnlineUpdate[0]: $it")
    }
  }
  
  launch {
    delay(3500)
    sessionOnlineUpdateEvents.collect {
      println("onSessionOnlineUpdate[1]: $it")
    }
  }
}