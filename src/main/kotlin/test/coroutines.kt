package test.coroutines

import kotlinx.coroutines.*


fun main(){
    coroutines()
}



// A coroutine is an instance of suspendable computation.
// Coroutines do not create separate thread

/*
    ● runBlocking {}:
        The name of runBlocking means that the thread that runs it (in this case — the main thread)
        gets blocked for the duration of the call,
        until all the coroutines inside runBlocking { ... } complete their execution.

    ● launch {}:
        launch is a coroutine builder.
        It launches a new coroutine concurrently with the rest of the code,
        which continues to work independently.

    ● delay(<time millis>):
        is a special suspending function. It suspends the coroutine for a specific time.
        Suspending a coroutine does not block the underlying thread,
        but allows other coroutines to run and use the underlying thread for their code.
 */
private fun coroutines(){
    fun main() = runBlocking { // this: CoroutineScope
        launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello") // main coroutine continues while a previous one is delayed
    }
    main() // prints "Hello" then "World!"



    runBlocking {
        val one = suspendableGetOne()
    }


    fun main2() = runBlocking {
        launch { doWorld() }
        println("Hello")
    }
    main2()

}



private suspend fun suspendableGetOne(): Int {
    return 1
}

private suspend fun doWorld(){
    delay(1000L)
    println("World!")
}




