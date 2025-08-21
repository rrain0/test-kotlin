package com.rrain.util.base.any

import com.rrain.util.base.bool.bool


inline fun <reified T> Any?.cast(): T = this as T


// Maps value by block if it == null
// Analog for (value ?: defaultValue) but can be used without ()
fun <T : Any?>T.mapNull(block: () -> T & Any): T & Any {
  return this ?: block()
}

// Maps value by block if it casts to true
fun <T : Any?>T.mapTruly(block: (it: T) -> T): T {
  if (this.bool) return block(this)
  return this
}

