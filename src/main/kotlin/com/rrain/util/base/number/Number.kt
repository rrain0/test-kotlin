package com.rrain.util.base.number



inline fun Int.mapZero(block: () -> Int): Int = (
  if (this == 0) block() else this
)
inline fun Double.mapZero(block: () -> Double): Double = (
  if (this == 0.0) block() else this
)