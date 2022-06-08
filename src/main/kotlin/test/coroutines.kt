package test.coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


fun main(){
    //coroutines()
    //test2()
    //test3Job()
    //test4JobCancellation()
    //test5CheckIsActiveToCancel()
    //test6CancelAndFinally()
    //test7NonCancellable()
    //test8UseTimeout()
    //test8UseTimeoutOrNull()
    //test9Async()
    //test10AsyncLazily()
    //test11AsyncFunctions()
    //test12()
    test13CancellationPropagation()
}



// A coroutine is an instance of suspendable computation.
// Coroutines do not create separate thread

/*
    ● runBlocking {}:
        The name of runBlocking means that the thread that runs it (in this case — the main thread)
        gets blocked for the duration of the call,
        until all the coroutines inside runBlocking { ... } complete their execution.

    ● coroutineScope {}:
        waits for all nested coroutines complete then completes itself

    ● launch {}:
        launch is a coroutine builder.
        It launches a new coroutine concurrently with the rest of the code,
        which continues to work independently.
        Returns Job.

    ● async {}:
        it is like launch {}, but returns result value.
        Returns Deferred.

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


/*
    ● coroutineScope {}:
        runBlocking and coroutineScope builders may look similar because
        they both wait for their body and all its children to complete.
        The main difference is that the runBlocking method blocks the current thread for waiting,
        while coroutineScope just suspends, releasing the underlying thread for other usages.
 */
private suspend fun doHelloWorld() = coroutineScope {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello")
}







/*
    Prints:
        Hello
        World 1
        World 2
        Done
 */
private fun test2() = runBlocking {
    doHelloWorld12()
    println("Done") // doesn't print until doHelloWorld() complete
}
// next code doesn't execute until all coroutines complete inside coroutineScope {}
private suspend fun doHelloWorld12() = coroutineScope {
    // two launch {} are working concurrently
    launch {
        delay(2000L)
        println("World 2")
    }
    launch {
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}











private fun test3Job() = runBlocking {
    val job:Job = launch { // launch a new coroutine and keep a reference to its Job
        delay(1000L)
        println("World!")
    }
    println("Hello")
    job.join() // wait util child coroutine completes
    println("Done")
}





/*
    Coroutine cancellation is cooperative.
    A coroutine code has to cooperate to be cancellable.
    All the suspending functions in kotlinx.coroutines are cancellable.
    They check for cancellation of coroutine and throw CancellationException when cancelled.
 */
private fun test4JobCancellation() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    job.join() // waits for job's completion
    //job.cancelAndJoin()
    println("main: Now I can quit.")
}





private fun test5CheckIsActiveToCancel() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive){ // cancellable computation loop
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime){
                println("job: I'm sleeping ${i++}")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}




/*
    Cancellable suspending functions throw CancellationException on cancellation which can be handled in the usual way.
    For example, try {...} finally {...} expression and Kotlin use function execute
    their finalization actions normally when a coroutine is cancelled
 */
private fun test6CancelAndFinally() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("job: I'm running finally")
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion (and finalization)
    println("main: Now I can quit.")
}





// NonCancellable if cancelled
private fun test7NonCancellable() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancels the job and waits for its completion
    println("main: Now I can quit.")
}





// Cancel via useTimeout(){}
// throws kotlinx.coroutines.TimeoutCancellationException if timed out
private fun test8UseTimeout() = runBlocking {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}




// Cancel via useTimeoutOrNull(){}
// returns null if timed out
private fun test8UseTimeoutOrNull() = runBlocking {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" // will get cancelled before it produces this result
    }
    println("Result is $result")
}



// Two coroutines execute concurrently.
// Note that concurrency with coroutines is always explicit.
private fun test9Async() = runBlocking {
    val time = measureTimeMillis {
        val one:Deferred<Int> = async { doSomethingUsefulOne() }
        val two:Deferred<Int> = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}
private suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}
private suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}



// if async is lazy, then it starts when <deferred>.start() or when <deferred>.await().
// await() blocks until gets result.
private fun test10AsyncLazily() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        two.start()
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}



// Async Funcitons
private fun test11AsyncFunctions() {
    val time = measureTimeMillis {
        // we can initiate async actions outside of a coroutine
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()
        // but waiting for a result must involve either suspending or blocking.
        // here we use `runBlocking { ... }` to block the main thread while waiting for the result

        // ATTENTION!!! if after call async function, there is exception before await(),
        // the async computations doesn't stop. They can be stopped only manually or themselves when completes.
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }
    println("Completed in $time ms")
}
@OptIn(DelicateCoroutinesApi::class)
private fun somethingUsefulOneAsync() = GlobalScope.async {
    delay(1000L) // pretend we are doing something useful here
    13
}
@OptIn(DelicateCoroutinesApi::class)
private fun somethingUsefulTwoAsync() = GlobalScope.async {
    delay(1000L) // pretend we are doing something useful here, too
    29
}






private fun test12() = runBlocking {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
}
// if any error, then all coroutines in the scope will be cancelled
private suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}












// Cancellation is always propagated through coroutines hierarchy:
private fun test13CancellationPropagation() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch(e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }

}
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            // Emulates very long computation, will be cancelled if any error in this coroutine scope
            delay(Long.MAX_VALUE)
            42
        } finally {
            println("First child was cancelled")
        }
    }

    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }

    one.await() + two.await()
}




