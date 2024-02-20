package test.multithreading

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

private var size = 1000

private val list1: MutableList<Int> = Collections.synchronizedList(ArrayList())
private val list2: MutableList<Int> = Collections.synchronizedList(ArrayList())

private val counter: AtomicInteger = AtomicInteger()
private val resultList: MutableList<Int?> = Collections.synchronizedList(ArrayList())

fun main0(){

    repeat(size) {
        list1.add(Random().nextInt())
        list2.add(Random().nextInt())
        resultList.add(null)
    }

    val threadPool = Executors.newFixedThreadPool(3)

    repeat(size) { threadPool.submit(::sum) }

    threadPool.shutdown()
    threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS)

    println(list1)
    println(list2)
    println(resultList)
}


private fun sum(){
    val i = counter.getAndIncrement()
    resultList[i] = list1[i] + list2[i]
}