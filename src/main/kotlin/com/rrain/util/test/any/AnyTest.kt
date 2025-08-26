package com.rrain.util.test.any

import kotlin.random.Random
import kotlin.random.nextInt




fun main() {
  val v: String? = Random.nextInt(0..1).let { if (it == 0) null else "str" }
  
  run {
    val a = v.`??`("null")
    val af = v.`??` { "null" }
    val b = v.`?!`("not null")
    val bf = v.`?!` { "not null" }
  }
  run {
    val a = v `??` "null"
    val af = v `??` { "null" }
    val b = v `?!` "not null"
    val bf = v `?!` { "not null" }
  }
  run {
    val a = v`??` "null"
    val af = v`??` { "null" }
    val b = v`?!` "not null"
    val bf = v`?!` { "not null" }
  }
}


// Replace null
infix fun <T> T.`??`(replacement: T & Any): T & Any = this ?: replacement
inline infix fun <T> T.`??`(block: () -> T & Any): T & Any = this ?: block()

// Replace not null
infix fun <T, R> T.`?!`(replacement: R): R? = this?.let { replacement }
inline infix fun <T, R> T.`?!`(block: (T & Any) -> R): R? = this?.let(block)
