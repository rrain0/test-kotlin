package `event-engine`.`live-object`

import com.rrain.util.base.`date-time`.now
import com.rrain.util.base.uuid.randomUuid
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.days




fun main() {
  liveObject()
  val (int, str) = run { listOf(1, "s") }
}


fun liveObject() = runBlocking(Executors.newCachedThreadPool().asCoroutineDispatcher()) {
  val id = randomUuid()
  val userId = randomUuid()
  val expiresAt = now() + 10.days
  
  launch {
    SessionData(id, expiresAt, userId, now(), true).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdateSession(it)
    }
    
    delay(1000)
    SessionData(id, expiresAt, userId, now(), true).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdateSession(it)
    }
    
    delay(2000)
    SessionData(id, expiresAt, userId, online = false).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdateSession(it)
    }
    
    delay(1000)
    SessionData(id, expiresAt, userId, now(), true).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdateSession(it)
    }
  }
  
  
  launch {
    delay(100)
    
    val cnt = AtomicInteger(0)
    
    // launch(start = CoroutineStart.UNDISPATCHED) { } -> Job:
    //   executes immediately in current thread until first suspension point
    //   even if coroutine was already cancelled.
    launch(start = CoroutineStart.UNDISPATCHED) {
      
      // sharedFlow.collect guarantees to subscribe immediately and to be ready to receive emissions.
      sessionOnlineUpdateEvents.collect {
        println("onSessionOnlineUpdate[0][${cnt.getAndIncrement()}]: $it")
      }
    }
    
    // In any case get current state, and then it will be updated via SharedFlow.
    println("onSessionOnlineUpdate[0][${cnt.getAndIncrement()}]: ${LiveSession.getSession(id)}")
  }
  
  launch {
    delay(3500)
    
    val cnt = AtomicInteger(0)
    launch(start = CoroutineStart.UNDISPATCHED) {
      sessionOnlineUpdateEvents.collect {
        println("onSessionOnlineUpdate[1][${cnt.getAndIncrement()}]: $it")
      }
    }
    
    println("onSessionOnlineUpdate[1][${cnt.getAndIncrement()}]: ${LiveSession.getSession(id)}")
  }
}

