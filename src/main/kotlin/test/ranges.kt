
package test.ranges

import geometry.Point2
import kotlin.math.sqrt




fun main(){
    //ranges()
    customRange()
}

/*
    RANGES

    Kotlin lets you easily create ranges of values using the rangeTo() function from the kotlin.ranges package
    and its operator ..
    Usually, rangeTo() is complemented by in or !in functions.

    !!! Attention 10L in 0..100 is false because type mismatch (Long value not in Int range)
 */

private fun ranges() {
  
  run {
    var i = 4
    if (i in 1.rangeTo(4)) { // equivalent of 1 <= i && i <= 4
        print(i)
    }
  }
  
  // equivalent to previous
  for (i in 1..4) print(i)
  
  // reverse order
  for (i in 4 downTo 1) print(i)
  for (i in (1..4).reversed()) print(i)
  
  // with step:
  for (i in 1..8 step 2) print(i)
  for (i in 8 downTo 1 step 2) print(i)
  
  // without last elem:
  for (i in 1 until 10) { // i in 1 until 10, excluding 10
      print(i)
  }
  // kotlin 1.9+ syntax
  for (i in 1..<10) { // i in 1 until 10, excluding 10
      print(i)
  }
  
  
  
  // Ranges are defined for comparable types: having an order, you can define whether an arbitrary instance
  // is in the range between two given instances.
  // I can create iteration over 2d line between 2 points
  /*
      TODO implement
      val versionRange = Version(1, 11)..Version(1, 30)
      println(Version(0, 9) in versionRange)
      println(Version(1, 20) in versionRange)
  */
  
  1000000..3000000000 step 5
  
  // Progressions implement Iterable<N>, where N is Int, Long, or Char respectively,
  // so you can use them in various collection functions like map, filter, and other.
  println((1..10).filter { it % 2 == 0 })
  
  
  println((0..5).joinToString(",",transform={":val$it"}))
  
  
  println(('a'..'z').joinToString(", ", transform = {"char: $it"}))
  println(('z' downTo 'a').joinToString(", ", transform = {"char: $it"}))
  
}


private fun customRange(){
    run {
        val a = Point2(2.0,2.0)
        val b = Point2(4.0, 8.0)
        
        println("$a..$b:")
        
        for (pt in a..b){
            println(pt)
        }
        println()
    }
    run {
        val a = Point2(2.0,2.0)
        val b = Point2(4.0, 4.0)
        
        println("$a..$b:")
        
        for (pt in a..b){
            println(pt)
        }
        println()
    }
    run {
        val a = Point2(2.0,2.0)
        val b = Point2(4.0, 4.0)
        
        println("Point(3.0,3.0) in $a..$b step ${sqrt(2.0)}: ${Point2(3.0,3.0) in a..b step sqrt(2.0)}") // true
        
        println()
    }
    run {
        val a = Point2(2.0,2.0)
        val b = Point2(8.0, 8.0)
        
        println("$a..$b step ${sqrt(2.0)}:")
        
        for (pt in a..b step sqrt(2.0)){
            println(pt)
        }
        println()
    }
    run {
        val a = Point2(2.0,2.0)
        val b = Point2(0.0, 0.0)
        
        println("$a..$b step ${sqrt(2.0)}:")
        
        for (pt in a..b step sqrt(2.0)){
            println(pt)
        }
        println()
    }
    run {
        val a = Point2(2.0,2.0)
        val b = Point2(0.0, 8.0)
        
        println("$a..$b:")
        
        for (pt in a..b){
            println(pt)
        }
        println()
    }
    run {
        val a = Point2(2.0,2.0)
        val b = Point2(10.0, 2.0)
        
        println("$a..$b:")
        
        for (pt in a..b){
            println(pt)
        }
        println()
    }
}















