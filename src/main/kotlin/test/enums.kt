package test

import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator

fun enumTest(){

    val down = Direction.DOWN
    println("Direction.DOWN: ${Direction.DOWN}") // DOWN
    println("Direction.DOWN.name: ${Direction.DOWN.name}") // DOWN
    println("Direction.DOWN.ordinal: ${Direction.DOWN.ordinal}") // 1

    println("Direction.values(): ${Direction.values().asList()}") // [UP, DOWN, LEFT, RIGHT]
    println("enumValues<T>() as enumValues<Direction>(): ${enumValues<Direction>().asList()}") // [UP, DOWN, LEFT, RIGHT]
    println("Direction.values(): ${Direction.valueOf("DOWN")}") // DOWN

    println(Color.RED) // THIS IS RED
}

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

private enum class Color(val rgb:Int) {
    RED(0xFF0000) {
        override fun getShortName() = "r"
        override fun toString() = "THIS IS RED"
    },
    GREEN(0x00FF00) {
        override fun getShortName() = "g"
    },
    BLUE(0x0000FF) {
        override fun getShortName() = "b"
    };

    abstract fun getShortName(): String
}

private enum class IntArithmetics : BinaryOperator<Int>, IntBinaryOperator {
    PLUS {
        override fun apply(t: Int, u: Int) = t + u
    },
    TIMES {
        override fun apply(t: Int, u: Int) = t * u
    };

    override fun applyAsInt(t:Int, u:Int) = apply(t,u)
}

