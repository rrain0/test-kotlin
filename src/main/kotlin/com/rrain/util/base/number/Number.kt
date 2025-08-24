package com.rrain.util.base.number



inline infix fun Int.mapZero(block: () -> Int): Int = (
  if (this == 0) block() else this
)
inline infix fun Double.mapZero(block: () -> Double): Double = (
  if (this == 0.0) block() else this
)