package math.`quadratic-equation`

import kotlin.math.sqrt


fun main() {
  println(solveQuadraticEquation(18.214, 128.824, 32.255))
}


fun solveQuadraticEquation(a: Double, b: Double, c: Double): List<Double> {
  val D = b * b - 4 * a * c
  if (D < 0) return emptyList()
  val sqrtD = sqrt(D)
  return listOf( (-b + sqrtD) / (2 * a), (-b - sqrtD) / (2 * a) )
}
