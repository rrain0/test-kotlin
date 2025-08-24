package kotlinlang.coroutines.Mutex

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors



fun main() {
  var cnt = 0
  
  Thread {
    cnt++
    Thread.sleep(10000)
  }.start()
  
  val mutex = Mutex()
  Executors.newCachedThreadPool().use { runBlocking(it.asCoroutineDispatcher()) {
    println("0: $cnt")
    
    launch {
      mutex.withLock {
        cnt++
        
        launch {
          Thread.sleep(1000)
          println("1: $cnt")
          Thread.sleep(5000)
        }
        launch {
          Thread.sleep(1000)
          println("2: ${++cnt}")
          Thread.sleep(5000)
        }
        delay(3000)
      }
    }
    
    launch {
      mutex.withLock {
        println("3: ${++cnt}")
      }
    }
    
  } }
}
