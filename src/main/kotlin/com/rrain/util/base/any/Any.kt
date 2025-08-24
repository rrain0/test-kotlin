package com.rrain.util.base.any

import com.rrain.util.base.bool.bool



inline fun <reified T> Any?.cast(): T = this as T


// Maps value by block if it == null
// Analog for (value ?: defaultValue) but can be used without ()
fun <T : Any?>T.ifNull(block: () -> T & Any): T & Any {
  return this ?: block()
}

// Maps value by block if it casts to true
fun <T : Any?>T.ifTruly(block: (it: T) -> T): T {
  if (this.bool) return block(this)
  return this
}



fun <T : Comparable<T>> maxOf(a: T?, b: T?): T? = when {
  b == null -> a
  a == null -> b
  a >= b -> a
  else -> b
}

