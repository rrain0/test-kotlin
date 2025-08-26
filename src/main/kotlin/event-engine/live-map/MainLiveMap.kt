package `event-engine`.`live-map`

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Expiry
import com.github.benmanes.caffeine.cache.RemovalCause
import com.rrain.util.base.`date-time`.now
import com.rrain.util.base.uuid.randomUuid
import com.rrain.util.base.uuid.toUuid
import dev.hsbrysk.caffeine.buildCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration




fun main() {
  liveObject()
  //caffeineCache()
}



fun liveObject() = Executors.newCachedThreadPool().use { runBlocking(it.asCoroutineDispatcher()) {
  val session1 = SessionData(
    id = "7e61151e-f6d3-4199-b50a-e8f00b1a97be".toUuid(),
    expiresAt = now() + 10.days,
    userId = "a6f555cd-c785-487a-813d-6a0ab2652ce5".toUuid(),
  )
  val session2 = SessionData(
    id = randomUuid(),
    expiresAt = now() + 10.days,
    userId = randomUuid(),
  )
  
  launch { UnusedSessions.events.collect {
    println("\nCACHE event: $it\n")
  } }
  
  val bgScope = CoroutineScope(it.asCoroutineDispatcher())
  initSessionStorage(
    bgScope = bgScope,
    cacheLifetime = 1.5.seconds,
  )
  
  
  launch {
    delay(1000)
    session1.copy(onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSessions.addOrUpdate(it)
    }
    
    delay(1000)
    session1.copy(onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSessions.addOrUpdate(it)
    }
    
    delay(2000)
    // Здесь мы сообщаем, что перестали быть онлайн в момент onlineAt
    session1.copy(onlineAt = now(), online = false).let {
      println("addOrUpdateSession: $it")
      LiveSessions.addOrUpdate(it)
    }
    
    delay(2000)
    session1.copy(onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSessions.addOrUpdate(it)
    }
    
    delay(1000)
    // Здесь мы сообщаем, что перестали быть онлайн.
    // В какой момент - неизвестно, так что onlineAt не передаём.
    println("removeSession: ${session1.id}")
    LiveSessions.remove(session1.id)
    
    delay(1000)
    session1.copy(onlineAt = now(), online = true).let {
      println("addOrUpdateSession: $it")
      LiveSessions.addOrUpdate(it)
    }
  }
  
  
  launch {
    val cnt = AtomicInteger(0)
    
    // launch(start = CoroutineStart.UNDISPATCHED) { } -> Job:
    //   executes immediately in current thread until first suspension point
    //   even if coroutine was already cancelled.
    launch(start = CoroutineStart.UNDISPATCHED) {
      
      // sharedFlow.collect guarantees to subscribe immediately and to be ready to receive emissions.
      OnlineSessionsShared.events.collect {
        println("onSessionOnlineUpdate[0][${cnt.getAndIncrement()}]: $it")
      }
    }
    
    // In any case get current state, and then it will be updated via SharedFlow.
    println("onSessionOnlineUpdate[0][${cnt.getAndIncrement()}]: ${LiveSessions.get(session1.id)}")
  }
  
  launch {
    delay(3500)
    
    val cnt = AtomicInteger(0)
    launch(start = CoroutineStart.UNDISPATCHED) {
      OnlineSessionsShared.events.collect {
        println("onSessionOnlineUpdate[1][${cnt.getAndIncrement()}]: $it")
      }
    }
    
    println("onSessionOnlineUpdate[1][${cnt.getAndIncrement()}]: ${LiveSessions.get(session1.id)}")
  }
} }




fun caffeineCache() = Executors.newCachedThreadPool().use { exec ->
  runBlocking(exec.asCoroutineDispatcher()) {
    val entryLifetime = 3.seconds
    val cacheAsync = Caffeine.newBuilder()
      .expireAfter<SessionId, SessionData>(
        Expiry.accessing { k, v ->
          when {
            v.online -> v.expiresAt - now()
            else -> minOf(v.expiresAt - now(), entryLifetime)
          }.also { println("duration: $it") }.toJavaDuration()
        }
      )
      .executor(exec)
      // Срабатывает только если сам кэш удаляет запись, а не пользователь снаружи.
      .evictionListener<SessionId, SessionData> { k, v, cause ->
        when (cause) {
          RemovalCause.EXPLICIT -> println("k: $k, v: $v, cause: $cause")
          RemovalCause.REPLACED -> println("k: $k, v: $v, cause: $cause")
          RemovalCause.COLLECTED -> println("k: $k, v: $v, cause: $cause")
          RemovalCause.EXPIRED -> println("k: $k, v: $v, cause: $cause")
          RemovalCause.SIZE -> println("k: $k, v: $v, cause: $cause")
        }
      }
      .buildCoroutine<SessionId, SessionData>()
    val cache = cacheAsync.synchronous()
    val cacheMap = cache.asMap()
    
    val session1 = SessionData(
      id = randomUuid(),
      userId = randomUuid(),
      expiresAt = now() + 10.days,
      onlineAt = null,
      online = false,
    )
    
    cacheMap[session1.id] = session1
    cacheMap -= session1.id
    
    delay(2500)
    cacheMap[session1.id] = session1.copy(onlineAt = now(), online = true)
    
    delay(3000)
    cache.cleanUp()
    
    delay(10000)
  }
}
