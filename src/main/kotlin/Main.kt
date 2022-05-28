import test.enums.enums
import test.reflection.reflection
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.pow

fun main(args: Array<String>) {
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    //println(listOf(1,2,3,0).sortedBy { it })



    val str = "fkldsfj"
    val strWithA = str.appendA()
}


fun String.appendA(): String {
    return this+"A"
}