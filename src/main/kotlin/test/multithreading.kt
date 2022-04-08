package test

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

fun multithreading(){


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


private class A{
    fun syncFun() = synchronized(this) {
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


    A().syncFun()


    val obj = Any()
    synchronized(obj) {

    }
    synchronized(obj, {

    })
}

