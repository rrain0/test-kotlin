package test.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.Semaphore
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.CompletionHandler
import java.util.concurrent.locks.ReentrantLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.nio.file.Path
import kotlin.concurrent.withLock


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
    //test13CancellationPropagation()

    //runBlocking { Flows.flowCountTakeDrop() }
    //runBlocking { Flows.flowMapAndTransform() }
    //runBlocking { Flows.flowFilterAndTakeWhileAndDropWhile() }
    //runBlocking { Flows.flowReduceAndFold() }
    runBlocking { Flows.flowZip() }

    // launch coroutine directly in global scope
    GlobalScope.launch {  }

    // create some scope in launch inside
    val workerScope = CoroutineScope(Dispatchers.IO)
    workerScope.launch {  }

    // create UI scope in launch inside
    // MainScope() uses Dispatchers.Main for its coroutines and has a SupervisorJob.
    val uiScope = MainScope()
    uiScope.launch {  }
}



// A coroutine is an instance of suspendable computation.
// Coroutines do not create separate thread

/*
    ● runBlocking {}:
        The name of runBlocking means that the thread that runs it
        gets blocked for the duration of the call,
        until all the coroutines inside runBlocking { ... } complete their execution.

    ● coroutineScope {}:
        waits for all nested coroutines complete then completes itself.
        unlike runBlocking - does not block the thread, only suspends it.
        Например если coroutineScope { launch { ... } }, то coroutineScope не завершится,
        пока job из этого launch не завершится.

    ● withContext(context) {}:
        Calls the specified suspending block with a given coroutine context,
        suspends until it completes, and returns the result.
            withContext(NonCancelable)
            withContext(Dispatchers.IO)
        ● Returns usual result of block execution.

    ● launch {}:
        launch is a coroutine builder.
        It launches a new coroutine concurrently with the rest of the code,
        which continues to work independently.
        ● Returns Job.

    ● async {}:
        it is like launch {}, but returns result value.
        ● Returns Deferred.

    ● delay(<time millis>):
        is a special suspending function. It suspends the coroutine for a specific time.
        Suspending a coroutine does not block the underlying thread,
        but allows other coroutines to run and use the underlying thread for their code.

    ● suspendCoroutine { continuation -> ... }
        Используется, чтобы превратить апи с коллбэками в suspend functions
        suspend fun getUser(id: String): User  = suspendCoroutine { continuation ->
            Api.getUser(id) { user ->
                continuation.resume(user)
            }
        }

    ● suspendCancellableCoroutine { cancellableContinuation -> ... }

    ● Channels for communication between coroutines
        A Channel is conceptually very similar to BlockingQueue.
        One key difference is that instead of a blocking 'put' operation it has a suspending 'send',
        and instead of a blocking 'take' operation it has a suspending 'receive'.

    ● Flows
        A regular Flow, such as defined by the flow { ... } function,
         which is COLD and is started separately for each collector.

    ● Shared Flows - broadcasting data to several receiver-coroutines
        A hotFlow that shares emitted values among all its collectors in a broadcast fashion,
        so that all collectors get all emitted values.
        A shared flow is called HOT because its active instance exists independently of the presence of collectors.

    ● Mutex (instead of ReentrantLock in sync world) and ● Semaphore
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

    val lock = ReentrantLock()
    val sync = lock.newCondition()
    val sem = Semaphore(1)

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
    launch can be started lazily.
 */
private fun test4JobCancellation() = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L) // throws CancellationException here if coroutine was cancelled
        }
    }
    delay(300L)
    job.start() // start lazy coroutine
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    job.join() // waits for job's completion
    // job.cancel() + job.join() => job.cancelAndJoin()
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
        } catch (e: CancellationException) {
            println("there is CancellationException: $e")
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



// cancel in async {}
private suspend fun cancelInAwait() = coroutineScope {

    // создаем и запускаем корутину
    val message = async {
        "some message"
    }
    // отмена корутины
    message.cancelAndJoin()

    try {
        // ожидаем получение результата
        println("message: ${message.await()}")
    } catch (e: CancellationException) {
        println("Coroutine has been canceled")
    }
    println("Program has finished")
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
        val three = async(start=CoroutineStart.LAZY) { doSomethingUsefulTwo() } // can be started via Deferred.start() or Deferred.await()
        println("The answer is ${one.await() + two.await() + three.await()}")
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












// Cancellation is always propagated through coroutines' hierarchy:
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





// suspendCoroutine: asynchronous read with callback transform into suspend function:
suspend fun AsynchronousFileChannel.readAsync(buf: ByteBuffer, start: Long = 0L): Int =
    suspendCoroutine { continuation ->
        read(buf, start, Unit, object : CompletionHandler<Int, Unit> {
            override fun completed(bytesRead: Int, attachment: Unit) {
                continuation.resume(bytesRead)
            }

            override fun failed(exception: Throwable, attachment: Unit) {
                continuation.resumeWithException(exception)
            }
        })
    }









private object CoroutineDispatchers {

    // Диспетчер корутины - пул потоков для её выполнения
    suspend fun coroutineDispatchers() = coroutineScope{
        /*
        ● Dispatchers.Default: CPU-intensive tasks.
        Применяется по умолчанию, если тип диспетчера не указан явным образом.
        Этот тип использует общий пул разделяемых фоновых потоков и подходит для вычислений,
        которые не работают с операциями ввода-вывода (операциями с файлами, базами данных, сетью)
        и которые требуют интенсивного потребления ресурсов центрального процессора.

        ● Dispatchers.IO: Read/Write operations.
        Использует общий пул потоков, создаваемых по мере необходимости,
        и предназначен для выполнения операций ввода-вывода (например, операции с файлами или сетевыми запросами).

        ● Dispatchers.Main: Android UI Thread.
        Применяется в графических приложениях, например, в приложениях Android или JavaFX.

        ● Dispatchers.Unconfined: корутина не закреплена четко за определенным потоком или пулом потоков.
        Она запускается в текущем потоке до первой приостановки.
        После возобновления работы корутина продолжает работу в одном из потоков, который сторого не фиксирован.
        Разработчики языка Kotlin в обычной ситуации не рекомендуют использовать данный тип.

        ● newSingleThreadContext и ● newFixedThreadPoolContext: позволяют вручную задать поток/пул для выполнения корутины
         */

        launch(Dispatchers.Default) { }

        val pool = newFixedThreadPoolContext(2, "Double Threads")
        val job = launch(pool) { }
        job.join()
        pool.close()


        // Limited parallelism.
        // Produces dispatchers that limits usage of underlying threads.
        val backgroundDispatcher = newFixedThreadPoolContext(4, "App Background")
        // At most 2 threads will be processing images as it is really slow and CPU-intensive
        val imageProcessingDispatcher = backgroundDispatcher.limitedParallelism(2)
        // At most 3 threads will be processing JSON to avoid image processing starvation
        val jsonProcessingDispatcher = backgroundDispatcher.limitedParallelism(3)
        // At most 1 thread will be doing IO
        val fileWriterDispatcher = backgroundDispatcher.limitedParallelism(1)

    }
}





private object Channels {
    private suspend fun channelsBasics() = coroutineScope {
        // Создание канала для интов.
        // По умолчанию каналы без буфера, т.е. отправка-получение происходит
        // когда и отправитель и получатель готовы (вызваны методы send & receive).
        val intChannel = Channel<Int>(/*Channel.UNLIMITED*/)

        launch {
            for (n in 1..5){
                // sending through channel
                intChannel.send(n)
            }
            intChannel.close()  // Закрытие канала
        }

        // receiving data from channel
        repeat(5){
            val number = intChannel.receive()
        }
        println("End")
    }



    // Паттерн producer-consumer
    fun CoroutineScope.getNames(): ReceiveChannel<String> = produce {
        val names = listOf("Tom","Bob","Sam")
        for (name in names) {
            send(name)
        }
    }
    suspend fun channelsProducerConsumerTest() = coroutineScope {
        val names = getNames()
        // consumeEach закрывает канал
        // for (item in channel) не закрывает канал
        names.consumeEach { println(it) }
        println("End")
    }
}







private object Flows {
    fun getNames(): Flow<String> = flow {
        val names = listOf("Tom","Bob","Sam")
        for ((i, item) in names.withIndex()){
            delay(400L) // имитация продолжительной работы
            println("Emit ${i+1} item")
            emit(item) // эмитируем значение
        }
    }

    suspend fun proceedNames() = coroutineScope {
        val namesFlow = getNames() // поток создан, но не запущен
        namesFlow.collect{ println(it) } // запуск потока
    }

    suspend fun flowCreations(){
        val flowByFlowOf = flowOf(1,2,3,4,5,6)
        var flowByAsFlow = (1..6).asFlow()
        flowByAsFlow = listOf(1,2,3,4,5,6).asFlow()
    }

    /*
    Терминальные функции потоков

    Терминальные функции потоков (● Terminal operators) представляют suspend-функции, которые позволяют непосредственно получать объекты из потока или возвращают какое-то конечное значение:

        ● collect(): получает из потока переданные значения

        ● toList(): преобразует поток значений в коллекцию List

        ● toSet(): преобразует поток значений в коллекцию Set

        ● first() / firstOrNull(): получает первый объект из потока
          может принимать условие

        ● last() / lastOrNull(): получает последний объект из потока

        ● single() / singleOrNull(): ожидает получение одного объекта из потока
          Если 0 элементов, то NoSuchElementException.
          Если 2+ элементов, то IllegalStateException.

        ● count(): получает количество элементов в потоке
          может принимать условие

        ● reduce(): получает результат определенной операции над элементами потока

        ● fold(): получает результат определенной операции над элементами потока, в отличие от функции reduce() принимает начальное значение

    Промежуточные функции

    Промежуточные функции (● Intermediate operators) принимают поток и возвращают обработанный поток.

        ● combine(): объединяет два потока в один, после применения к их элементам функции преобразования

        ● drop(): исключает из начала потока определенное количество значений и возвращает полученный поток

        ● take(count): выбирает из потока определенное количество элементов

        ● takeWhile( (element)->Boolean ): берёт из потока первые элементы, соответсвующие условию

        ● filter(): фильтрует поток, оставляя те элементы, которые соответствуют условию

        ● filterNot(): фильтрует поток, оставляя те элементы, которые НЕ соответствуют условию

        ● filterNotNull(): фильтрует поток, удаляя все элементы, которые равны null

        ● onEach(): применяет к элементам потока определенную функцию перед тем, как они будут переданы в возвращаемый поток

        ● map(): применяет к элементам потока фукцию преобразования

        ● transform(): применяет к элементам потока функцию преобразования

        ● zip(): из двух потоков создает один, применяя к их элементам функцию преобразования

     */

    suspend fun flowCountTakeDrop() = coroutineScope{
        /*
            Creates a cold flow from the given suspendable block.
            The flow being COLD means that the block is called
            every time a terminal operator is applied to the resulting flow.
         */
        val flow = flow {
            (1..10).forEach {
                delay(400L)
                emit(it)
            }
        }

        println("#0")

        delay(1000L)
        // every call of count() runs flow  code-block, so we need wait 5 seconds (1000, then 400*10)
        println("#1, flow.count: ${flow.count()}")

        delay(1000L)
        println("#2, flow.count: ${flow.count { it%2==0 }}") // counts only even numbers

        delay(1000L)
        println("#3:")
        // снова запускает flow block и выводит числа по мере их получения
        flow.filter { it%3!=0 }.collect(::println)

        println("#4 take(2):")
        flow.take(2).collect(::println) // взять только первые 2 элемента

        println("#5 drop(2):")
        flow.drop(2).collect(::println) // пропустить первые 2 элемента
    }

    suspend fun flowMapAndTransform(): Unit = coroutineScope {
        data class Person(val name: String, val age: Int)

        val peopleFlow = flowOf(
            Person("Tom", 37),
            Person("Sam", 15),
            Person("Bob", 21),
        )

        peopleFlow.map{ it.name }.collect(::println)

        peopleFlow.transform {
            emit( object{
                val name = it.name
                val isAdult = it.age >= 18
            })
        }.collect{ println("name: ${it.name}, isAdult: ${it.isAdult}") }

        val numberFlow = flowOf(1,2,3,4)

        // можем эмитить сколько угодно
        numberFlow.transform {
            emit(it)
            emit(it*it)
        }.collect(::println)

    }

    suspend fun flowFilterAndTakeWhileAndDropWhile(): Unit = coroutineScope {
        data class Person(val name: String, val age: Int)

        val peopleFlow = flowOf(
            Person("Tom", 37),
            Person("Sam", 15),
            Person("Bob", 21),
            Person("Jack", 10),
            Person("Joe", 22),
        )

        println()
        println("filter { it.name.startsWith('j', true) }:")
        peopleFlow.filter { it.name.startsWith('j', true) }.collect(::println)

        println()
        println("takeWhile { it.age > 20 }:")
        // брать элементы из потока, пока выполняется условие
        // если условие хоть раз не выполнилось, процесс завершается
        peopleFlow.takeWhile { it.age > 20 }.collect(::println)

        println()
        println("dropWhile { it.age == 21 }:")
        // удаляет из потока первые элементы, пока они соответсвуют условию
        // берёт текущий элемент, для которого не выполнилось условие и все последующие
        peopleFlow.dropWhile { it.age == 37 || it.age == 15 || it.age == 22 }.collect(::println)
    }


    suspend fun flowReduceAndFold(): Unit = coroutineScope {

        val numberFlow = flowOf(1,2,3,4)

        println()
        println("reduce to sum:")
        numberFlow.reduce{ a,b -> a+b }.let { println(it) }

        println()
        println("fold to sum-10:")
        // fold allows initial value compared with reduce
        numberFlow.fold(-10) { a,b -> a+b }.let { println(it) }

    }


    suspend fun flowZip(): Unit = coroutineScope {
        val english = flowOf("red", "yellow", "blue")
        val russian = flowOf("красный", "желтый", "синий")

        println()
        println("zip:")
        english.zip(russian) { a,b -> "$a: $b" }.collect(::println)
    }
}






private class MutexTest {

    // Old synchronized way
    val lock = ReentrantLock(true)
    val sync = lock.newCondition()

    fun lockedAction() {
        lock.lock()
        try {
            // some action
        } finally {
            lock.unlock()
        }
    }
    fun lockedAction2() = lock.withLock {
        // some action
    }


    // New coroutine way
    val mutex = Mutex()

    suspend fun suspendLockedAction() {
        mutex.lock()
        try {
            // some action
        } finally {
            mutex.unlock()
        }
    }
    suspend fun suspendLockedAction2() = mutex.withLock {
        // some action
    }

}



private class SharedResourceCoroutinesSync {

    private val ids: MutableSet<String> = mutableSetOf()

    // passes only one coroutine at a time
    private val mutex = Mutex()
    // Shared flow gives all waiters its values
    private val signal = MutableSharedFlow<Unit>(0,1,BufferOverflow.DROP_OLDEST)

    // only one task with the same id can be executed at a time
    suspend fun submitTaskById(
        id: String,
        task: suspend ()->Unit
    ): Job = coroutineScope {

        while (true){

            // analogue of entering  java synchronized block
            mutex.lock() // only one coroutine at once can pass, others suspends

            if (id !in ids){ // check if this id is busy

                ids += id // if not busy then make it busy

                // analogue of leaving java synchronized block
                mutex.unlock() // allow other coroutines to check

                break // go out from loop to do task
            }

            // if this id is busy, we need to wait, and need to be notified

            // subscribe on shared flow events, then unlock mutex
            // subscribe BEFORE unlock, so other coroutines can't push notifyAll events before we can receive them
            signal.onSubscription {
                // analogue of leaving java synchronized block
                mutex.unlock()
            }
                .take(1)
                .collect() // analogue of java blocking wait()
        }

        // returns Job
        launch {
            task() // executes some task
            mutex.withLock { // analogue of java synchronized block
                ids -= id // release this id
                signal.emit(Unit) // analogue of java notifyAll()
            }
        }
    }

}



