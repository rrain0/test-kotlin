package test

import kotlin.random.Random

fun randomTest(){

  val randomInts = List(10) { Random.nextInt(0 /* inclusive */, 100 /* exclusive */) }
  println(randomInts)

  val randomDoubles = List(10){ Random.nextDouble(-1000.0 /* inclusive */, 1000.0 /* exclusive */) }
  println(randomDoubles)
}