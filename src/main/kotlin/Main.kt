import test.enums.enums
import test.reflection.reflection
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    println("Hello Kotlin! ${listOf(1,2,3,0).sortedBy { it }}")



    //timeMeasurement()
    val list: List<String> = listOf("dsf")
    val mutableList: MutableList<String> = mutableListOf("dsf")
}


private fun timeMeasurement(){
    val time = measureTimeMillis {
        repeat(10000) { println("AAA") }
    }
    println("measured time: $time")
}


