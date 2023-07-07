package test

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock




fun main(){
  javaThreadLocal()
}




fun someElseMultithreading(){
  val lock = ReentrantLock()
  val condition = lock.newCondition()

  // Note that withLock is a Kotlin-provided extension function
  // that takes care of calling Lock.lock()/Lock.unlock() before/after invoking the supplied lambda.
  lock.withLock {           // like synchronized(lock)
    /*...*/
    condition.await()     // like wait()
    /*...*/
    condition.signal()    // like notify()
    /*...*/
    condition.signalAll() // like notifyAll()
    /*...*/
  }



}





// you can use extensions to add back the wait, notifyAll...
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
private fun Any.wait() = (this as java.lang.Object).wait()



private class Volatileable{
  // JVM volatile property
  @Volatile var volatileProp = 5
}


// To synchronize use synchronized block or @Synchronized annotation
private class SyncFun{
  fun syncFun() = synchronized(this) {
    // wait()/notifyAll() are discouraged, but you still can call it:
    (this as java.lang.Object).notifyAll()
  }

  @Synchronized
  fun syncFun2() {
    // wait()/notifyAll() are discouraged, but you still can call it:
    (this as java.lang.Object).notifyAll()
  }

}

fun oldThreads(){
  val t = Thread {
    Thread.sleep(3000)
    println("World!")
  }
  t.start()
  println("Hello")


  SyncFun().syncFun()


  val obj = Any()
  synchronized(obj) {

  }
  synchronized(obj, {

  })
}




private fun javaThreadLocal(){

  val threadLocalInt = object : ThreadLocal<Int>() {
    override fun initialValue() = 0
  }

  Thread {
    threadLocalInt.set(50)
    println("2nd thread threadLocalInt.get(): ${threadLocalInt.get()}") // 50
  }.apply {
    start()
    join()
  }

  println("main thread threadLocalInt.get(): ${threadLocalInt.get()}") // 0

}
