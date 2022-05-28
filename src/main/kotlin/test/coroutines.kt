package test.coroutines



import kotlinx.coroutines.*



fun coroutines(){
    // A coroutine is an instance of suspendable computation.
    // Coroutines do not create separate thread

    fun main() = runBlocking { // this: CoroutineScope
        launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello") // main coroutine continues while a previous one is delayed
    }
    main()

    runBlocking {
        val one = suspendableGetOne()
    }

}



suspend fun suspendableGetOne(): Int {
    return 1
}




