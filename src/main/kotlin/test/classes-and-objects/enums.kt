package test.`classes-and-objects`.enums

import java.util.function.BinaryOperator
import java.util.function.IntBinaryOperator
import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries


fun main(){
  enums()
}


fun enums(){
  val down = Direction.DOWN
  println("Direction.DOWN: ${Direction.DOWN}") // DOWN
  println("Direction.DOWN.name: ${Direction.DOWN.name}") // DOWN
  println("Direction.DOWN.ordinal: ${Direction.DOWN.ordinal}") // 1

  val directionEnumEntries: EnumEntries<Direction> = enumEntries<Direction>()
  val directionEntries: EnumEntries<Direction> = Direction.entries
  val directionEntriesList: List<Direction> = Direction.entries

  println("Direction.entries: ${Direction.entries}") // [UP, DOWN, LEFT, RIGHT]

  println("enumValues<T>() as enumValues<Direction>(): ${enumValues<Direction>().asList()}") // [UP, DOWN, LEFT, RIGHT]
  println("Direction.values(): ${Direction.valueOf("DOWN")}") // DOWN

  println(Color.RED) // THIS IS RED

  println("AnyNames.entries: ${AnyNames.entries}")

  println("enumValues<Direction>: ${enumValues<Direction>()}")
  println("enumValueOf<Direction>(\"UP\"): ${enumValueOf<Direction>("UP")}")
  //println("enumValueOf<Direction>(\"up\"): ${enumValueOf<Direction>("up")}") // java.lang.IllegalArgumentException: No enum constant test.enums.Direction.up

  println("Direction.UP is Direction: ${Direction.UP is Direction}") // true
  println("Direction.UP is Enum<*>: ${Direction.UP is Enum<*>}") // true
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

// обратные кавычки ` ` можно использовать для любого имени, как и у имени пакета / переменной / функции / класса ...
private enum class AnyNames {
  FIRST, second, третий, `4-ый`
}

