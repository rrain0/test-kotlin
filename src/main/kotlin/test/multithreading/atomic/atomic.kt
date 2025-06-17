package test.multithreading.atomic

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.asJavaAtomic
import kotlin.concurrent.atomics.asKotlinAtomic
import kotlin.concurrent.atomics.plusAssign


fun main() {

}


@OptIn(ExperimentalAtomicApi::class)
suspend fun kotlinAtomic() {
  // Initializes the atomic counter for processed items
  val processedItems = AtomicInt(0)
  val totalItems = 100
  val items = List(totalItems) { "item$it" }
  // Splits the items into chunks for processing by multiple coroutines
  val chunkSize = 20
  val itemChunks = items.chunked(chunkSize)
  coroutineScope {
    for (chunk in itemChunks) {
      launch {
        for (item in chunk) {
          println("Processing $item in thread ${Thread.currentThread()}")
          processedItems += 1 // Increment counter atomically
        }
      }
    }
  }
}




@OptIn(ExperimentalAtomicApi::class)
fun atomicKotlinToJavaAndViseVersa() {
  val kotlinAtomic = AtomicInt(42)
  val javaAtomic: AtomicInteger = kotlinAtomic.asJavaAtomic()
  println("Java atomic value: ${javaAtomic.get()}")
  // Java atomic value: 42
  
  // Converts Java's AtomicInteger back to Kotlin's AtomicInt
  val kotlinAgain: AtomicInt = javaAtomic.asKotlinAtomic()
  println("Kotlin atomic value: ${kotlinAgain.load()}")
  // Kotlin atomic value: 42
  
}

