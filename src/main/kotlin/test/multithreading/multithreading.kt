package test.multithreading

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.StampedLock
import kotlin.concurrent.withLock




fun main(){
  javaThreadLocal()
}




private fun testReentrantLock(){
  val lock = ReentrantLock(true)
  val condition = lock.newCondition()
  
  // Можно блокировать одним потоком несколько раз, но нужно разблокировать ровно столько же раз.

  // Note that withLock is a Kotlin-provided extension function
  // that takes care of calling Lock.lock()/Lock.unlock() before/after invoking the supplied lambda.
  lock.withLock {         // like synchronized(lock)
    /*...*/
    condition.await()     // like object.wait()
    /*...*/
    condition.signal()    // like object.notify()
    /*...*/
    condition.signalAll() // like object.notifyAll()
    /*...*/
  }
  
  // old way:
  lock.lock()
  try {
    // do what you need
  } finally {
    lock.unlock()
  }
}

private fun testReentrantReadWriteLock(){
  val lock = ReentrantReadWriteLock(true)
  val condition = lock.writeLock().newCondition()
  // read condition is not supported
  
  // Можно блокировать одним потоком несколько раз, но нужно разблокировать ровно столько же раз.

  // Блокировать на чтение могут многие, пока нет ни одной блокировки на запись.
  // Блокировка на запись только одна.
  
  // Reader can't acquire write lock
}

private fun testStampedLock(){
  val lock = StampedLock()
  // does not support conditions
  
  
  var counter = 0 // допустим это общий ресурс
    
  fun write(){
    // выставляем блокировку на запись, блокирует поток, если надо ждать
    val stamp = lock.writeLock()
    try {
      var tmp = counter // read
      tmp++ // modify
      counter = tmp // write
    } finally {
      // разблокирует или кидает исключение, если блокировка не этого штампа
      lock.unlockWrite(stamp) // unlock
    }
    
  }
  
  fun optimisticRead(){
    var stamp = lock.tryOptimisticRead()
    var tmp = counter // optimistic read
    
    // В этот момент другой поток может изменить данные,
    // но к концу выполнения кода нам нужно чтобы начальные данные были те же.
    
    // Проверка, не был ли модифицирован общий ресурс после взятия оптимистичной блокировки.
    // Если была модификация, то читаем снова уже с нормальной блокировкой.
    if (!lock.validate(stamp)){
      // выставляем блокировку на чтение, блокирует поток, если надо ждать
      stamp = lock.readLock()
      try {
        tmp = counter // read
      } finally {
        // разблокирует или кидает исключение, если блокировка не этого штампа
        lock.unlockRead(stamp) // unlock
      }
    }
  }
  
}





// you can use extensions to add back the wait, notifyAll...
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
private fun Any.wait() = (this as java.lang.Object).wait()



private class JavaVolatile {
  // JVM volatile property
  @Volatile var volatileProp = 5
}


// To synchronize use synchronized block or @Synchronized annotation
private class SynchronizedFunction {
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


  SynchronizedFunction().syncFun()


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
