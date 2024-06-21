package test.destructuring

import java.io.BufferedReader
import java.io.FileReader


fun main() {
  destructuring()
}

fun destructuring() {
  
  // * - destructuring into vararg
  fun funVararg(vararg strings: String) {
    println(strings.toList())
  }
  funVararg("a", "str")
  funVararg(*arrayOf("a", "str"))
  funVararg(strings = *arrayOf("a", "str"))
  val abcdefg = listOf("d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
  funVararg(
    "a", "b", "c",
    // passes all content of any len, need exactly ARRAY
    *abcdefg.toTypedArray()
  )
  
  // var/val (<variables>) = something that have component[1,2,3...]() functions
  // only with var/val
  fun getPair() = 5 to "5"
  var (key, value) = getPair()
  val (key2, value2) = 6 to "6"
  var (a, b, c, d) = listOf(1, 2, 3, 4, 5)
  println("a=$a b=$b c=$c d=$d")
  
  // error: Destructuring declaration initializer of type List<Int> must have a 'component6()' function
  //var (a,b,c,d,e,f) = listOf(1,2,3,4,5,6)
  
  
  /*run {
  // Destructuring declaration initializer of type Array<Int> must have a 'component10()' function
  // Destructuring declaration initializer of type Array<Int> must have a 'component11()' function
  // Destructuring declaration initializer of type Array<Int> must have a 'component12()' function
  // Destructuring declaration initializer of type Array<Int> must have a 'component6()' function
  // Destructuring declaration initializer of type Array<Int> must have a 'component7()' function
  // Destructuring declaration initializer of type Array<Int> must have a 'component8()' function
  // Destructuring declaration initializer of type Array<Int> must have a 'component9()' function
    val (a, b, c, d, e, f, g, h, i, j, k, l) = arrayOf(1,2,3)
  }*/
  
  val firstComponent = listOf(1, 2, 3, 4, 5).component1()
  
  
  // destructuring in for loops
  for ((key, value) in mapOf("1" to 1));
  /*
      To do so, you must implement operators:
          operator fun <K, V> Map<K, V>.iterator(): Iterator<Map.Entry<K, V>> = entrySet().iterator()
          operator fun <K, V> Map.Entry<K, V>.component1() = getKey()
          operator fun <K, V> Map.Entry<K, V>.component2() = getValue()
   */
  
}