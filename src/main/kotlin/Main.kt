import test.enums.enums
import test.reflection.reflection
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
  // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.


  run {
    println("Hello Kotlin! ${listOf(1,2,3,0).sortedBy { it }}")


    val list: List<String> = listOf("dsf")
    val mutableList: MutableList<String> = mutableListOf("dsf")


    //timeMeasurement()


    listOf(1).forEach(fun(it){ println(it) })
    listOf(1).forEach { println(it) }



    val sum = fun(a:Int, b:Int): Int { return a+b }
    fun runIntAction(a: Int, b: Int, action: (Int,Int)->Int): Int {
      return action(a,b)
    }
    runIntAction(4, 10, sum)
    runIntAction(3,6, { a,b -> a*b })
    runIntAction(6,9) { a,b -> a-b }
  }
}



fun test_function() = 0
val test_function_lambda = { 0 }



private fun timeMeasurement(){
  val time = measureTimeMillis {
    repeat(10000) { println("AAA") }
  }
  println("measured time: $time")
}

