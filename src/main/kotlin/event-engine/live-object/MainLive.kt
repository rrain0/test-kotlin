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
}


fun liveObject() = runBlocking(Executors.newCachedThreadPool().asCoroutineDispatcher()) {
  val id = randomUuid()
  val userId = randomUuid()
  val expiresAt = now() + 10.days
  
  launch {
    delay(1000)
    SessionData(id, expiresAt, userId, onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdate(it)
    }
    
    delay(1000)
    SessionData(id, expiresAt, userId, onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdate(it)
    }
    
    delay(2000)
    // Здесь мы сообщаем, что перестали быть онлайн в момент onlineAt
    SessionData(id, expiresAt, userId, onlineAt = now(), online = false).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdate(it)
    }
    
    delay(1000)
    SessionData(id, expiresAt, userId, onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdate(it)
    }
    
    delay(1000)
    // Здесь мы сообщаем, что перестали быть онлайн.
    // В какой момент - неизвестно, так что onlineAt не передаём.
    println("removeSession: $id")
    LiveSession.remove(id)
    
    delay(1000)
    SessionData(id, expiresAt, userId, onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSession.addOrUpdate(it)
    }
  }
  
  
  launch {
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
    println("onSessionOnlineUpdate[0][${cnt.getAndIncrement()}]: ${LiveSession.get(id)}")
  }
  
  launch {
    delay(3500)
    
    val cnt = AtomicInteger(0)
    launch(start = CoroutineStart.UNDISPATCHED) {
      sessionOnlineUpdateEvents.collect {
        println("onSessionOnlineUpdate[1][${cnt.getAndIncrement()}]: $it")
      }
    }
    
    println("onSessionOnlineUpdate[1][${cnt.getAndIncrement()}]: ${LiveSession.get(id)}")
  }
}

