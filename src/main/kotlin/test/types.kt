/*
    You can create functions without classes - package level functions
    You can specify package or not. If not - function will be accessible from root
    You can run fun typesTest: types.typesTest()
    or import and run:

        import types.*
        ...
        typesTest()

    or:

        import types.typesTest
        ...
        typesTest()
*/

package test.types



fun main(){
  typesTest()
}


private fun typesTest() {

  // Java primitive / boxed types
  run {
    val booleanJavaPrimitiveType = Boolean::class.javaPrimitiveType
    val booleanJavaBoxedType = Boolean::class.javaObjectType
    println("booleanJavaPrimitiveType: $booleanJavaPrimitiveType") // boolean
    println("booleanJavaBoxedType: $booleanJavaBoxedType") // class java.lang.Boolean
    println()
  }

  // Nullable type
  run {
    // <any type>? - nullable type - тип может принимать значение null, если после имени типа поставить знак вопроса

    var list: List<Int>? = listOf(1, 2)

    // ?. - если list != null, то вернётся размер, иначе вернётся null
    var len = list?.size

    // ?: - оператор Элвис - если значение null, то вернуть указанное после ?:
    len = list?.size ?: 0
  }

  // Unit
  run {
    // Unit - используется как возвращаемый тип функции, если функция ничего не возвращает (void in Java)
    // Unit - The type with only one value: the Unit object. This type corresponds to the void type in Java.

    fun unit1() { }
    fun unit2(): Unit { }
    fun unit3(): Unit { return }
    fun unit4(): Unit { return Unit }
  }

  // Nothing
  run {
    // тип Nothing - ничего
    // описан в коде как:
    // public class Nothing private constructor()
    // Его нельзя инстанцировать

    // переменная типа Nothing не может принимать никакие значения - она всегда не инициализирована
    var a:Nothing

    // может быть только null
    var b:Nothing? = null

    var c:Unit? = Unit
    c = null

    // функция, возвращающая Nothing не может завершиться нормально
    fun throwException(): Nothing = throw Exception("exception")
    fun endlessLoop(): Nothing { while (true); }
    fun TODO(): Nothing = throw NotImplementedError()
  }


  // Any
  run {
    // Any - Root Class (like object in Java)
    // Имеет методы equals (==), hashCode, toString

    var any1: Any = ""
    any1 = 8

    var any2: Any? = null
  }




  // блок кода run {...} - иначе котлин считает это лямбдой

  // КОНСТАНТЫ
  run {

    // объявление константы, cannot be reassigned
    val num1:Int = 8

    // можно объявить без присваивания, но присвоить можно только раз
    // делать ничего с неинициализированной константой не получится
    val num2:Int
    num2 = 6



    // ПЕРЕМЕННЫЕ

    // ? разрешает значению быть null
    var num3:Int? = null
    num3 = 7

    // автовывод типа
    var num4 = 9
  }

  // В Kontlin - всё объекты (внутри они конечно могут быть оптимизированы, но снаружи - они объекты)

  // ЧИСЛА
  // В котлин нет (вообще нет, и в меньшую сторону тоже) неявных преобразований между числами!!! (тк всё объекты)
  // В арифметических операциях преобразования есть (как в Java)
  // JVM хранит объекты чисел (kotlin Int) в диапазоне байта [-128; 127]
  // и даже сравнение по ссылке тут окажется равным, но рассчитывать на это естественно не нужно

  // ЦЕЛЫЕ ТИПЫ
  run {
    var byte:Byte = 127 // 1 байт [-128; 127]
    var short:Short = 32767 // 2 байта [-32768; 32767]
    var int:Int = 2_147_483_647 // 4 байта [-2,147,483,648 (-2^31); 2,147,483,647 (2^31 - 1)]
    var long:Long = 9_223_372_036_854_775_807 // 8 байт [-9,223,372,036,854,775,808 (-2^63); 9,223,372,036,854,775,807 (2^63 - 1)]

    // по умолчанию Int, если вмещается, иначе Long
    var defaultInt = 7 // Int
    var defaultLong = 9_223_372_036_854_775_807 // Long

    // Литерал Long
    var literalLong = 9L // Long
    // Если написать просто число - то оно литерал инта (если не вмещается, то лонга)

    val binIntLiteral = 0b011
    val binLongLiteral1 = 0b011L
    val binLongLiteral2 = 0b011111111111111111111111111111111111111111111111111111111111
    // you cannot control sign inside bytes and you cant reach Long Min Value
    val binLongLiteral3 = 0b111111111111111111111111111111111111111111111111111111111111111

    val hexIntLiteral = 0xFFF
    val hexLongLiteral1 = 0xFFFL
    val hexLongLiteral2 = 0xFFFFFFFFFFFFF

    println("5 / 3: ${5 / 3}") // => 1 - деление целых типов всегда возвращает целый тип
  }

  // UNSIGNED INTEGER TYPES
  run {
    var ubyte:UByte = 255u // 1 байт [0; 255]
    var ushort:UShort = 65535u // 2 байта [0; 65535]
    var uint:UInt = 4_294_967_295u // 4 байта [0; 4_294_967_295 (2^32 - 1)]
    var ulong:ULong = 18_446_744_073_709_551_615u // 8 байт [0; 18_446_744_073_709_551_615 (2^64 - 1)]

    // по умолчанию UInt, если вмещается, иначе ULong
    var defaultUInt = 7 // UInt
    var defaultULong = 18_446_744_073_709_551_615u // ULong

    // Литерал ULong
    var literalLong = 9uL // ULong
    literalLong = 9UL // ULong
    // Если написать просто число - то оно литерал UInt (если не вмещается, то ULong)

    val binUIntLiteral = 0b011u
    val binULongLiteral1 = 0b011uL
    val binULongLiteral2 = 0b011111111111111111111111111111111111111111111111111111111111u
    // You CAN control leftmost bit !!!
    val binULongLiteral3 = 0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111u

    val hexUIntLiteral = 0xFFFu
    val hexULongLiteral1 = 0xFFFuL
    val hexULongLiteral2 = 0xFF_FF_FF_FF_FF_FF_FF_FFu

    // YOU CAN CONTROL SIGN !!!
    println("(0xFF_FF_FF_FF_FF_FF_FFu).toLong(): ${(0xFF_FF_FF_FF_FF_FF_FF_FFu).toLong()}") // => -1
    // get unsigned string representation of signed number:
    println("(6).toUInt().toString(2).padStart(32,'0'): " +
    (6).toUInt().toString(2).padStart(32,'0')
    ) // => 00000000000000000000000000000110 // all 32 bits
    println("(-6).toUInt().toString(2).padStart(32,'0'): " +
    (-6).toUInt().toString(2).padStart(32,'0')
    ) // => 11111111111111111111111111111010 // all 32 bits
  }

  // ДРОБНЫЕ ТИПЫ (с плавающей точкой)
  run {

    var float:Float = 127.5f // литерал Float (обязательно с f)
    var double:Double = 127.0 // литерал Double (обязательно с точкой)

    println("5 / 3.0: ${5 / 3.0}") // => 1.6666666666666667 - один из типов - дробный, так что всё преобразуется к дробным
  }

  // NUMBER TYPE CASTING
  run {
    // У каждого числового типа есть эти методы
    var a = 8
    var byte:Byte = 120.toByte()
    var short:Short = 80.toShort()
    var int:Int = 8.toInt()
    var long:Long = a.toLong()
    var float:Float = a.toFloat()
    var double:Double = a.toDouble()
    var char:Char = a.toChar()
    // есть ещё toUByte, toUShort, toUInt, toULong
    (120).toByte()
  }

  // BOOLEAN
  run {
    var bool: Boolean = true
    var bool2 = false as Boolean?
    println(bool && bool2?:false) // lazy AND
    println(bool || bool2!!) // lazy OR
    println(!bool) // NOT
  }

  // CHARACTER
  println(); println("CHARACTER:")
  run {
    val aChar: Char = 'a'

    // Special chars (escape sequences): \t \b \n \r \' \" \\ \$
    val newLine = '\n'
    val slash = '\\'
    // Any Unicode char: '\u****' e.g. '\uFF00' '\uff00'
    var unicodedChar = '\uFF00'
    unicodedChar = '\uff00'

    println("'a'.code: ${'a'.code}")
    println("'a'.category: ${'a'.category}")

    println("48.toChar(): ${48.toChar()}")

    //println(" ${'a'.digitToInt()}")
    println(" ${'9'.digitToIntOrNull()}")
    println(" ${'9'.digitToIntOrNull(2)}")
  }

  // ARRAYS
  run {
    println("ARRAYS")

    var ar1:Array<Int> = arrayOf(1,2,3)
    var ar2 = Array(5) { it*it } // функция-генератор, в которую передаётся индекс
    println("ar1: ${ar1.asList().toString()}")
    println("ar1: ${ar2.asList().toString()}")

    // Arrays have all collection methods
    ar1.filter { it!=1 }
    ar1.toList()
    ar1.toSet()
    ar1.associate { it to it }
    ar1.associateWith { it }
    ar1.asSequence()


    println("ar1 + ar2: ${(ar1+ar2).asList()}")
    println("ar1[2]: ${ar1[2]}")
    ar1[2] = 8
    println("ar1[2] = 8: ${ar1.asList()}")

    var arOfNulls:Array<String?> = arrayOfNulls<String>(5) // nullable array
    var arEmpty:Array<String> = emptyArray<String>() // array with len = 0

    // PRIMITIVE ARRAYS
    var arInt:IntArray = intArrayOf(4,6,) // primitive wrapped array
    var arUInt:UIntArray = uintArrayOf(4u,6u) // primitive wrapped unsigned array
    arUInt = arInt.toUIntArray()
    var boolArr: BooleanArray = booleanArrayOf(true, false, true)
  }

  // TYPE-CHECKED BRANCHES
  run {

    fun getStrLen(obj: Any): Int {
      if (obj is String) {
        // autocasted to String in this branch
        return obj.length
      }
      return 0
    }

    fun getStrLen2(obj: Any): Int {
      if (obj !is String) return 0
      // autocasted to String
      return obj.length
    }

    fun getStrLen3(obj: Any): Int {
      // autocast is immediate
      if (obj is String && obj.length > 0) return obj.length
      return 0
    }
  }

  // TYPE CAST
  run {
    // if cast is wrong then exception
    var o1: Any? = "lkdsjf"
    var o2: Any? = null
    o1 as String // cast to String
    (o2 as? Int)?.let {} // cast to Int if nonnull & doing smth
    o2 as Int?// cast to Int?

    o2.cast<Int?>()?.let {}// cast to Int? & doing smth
  }

}



// Cast function
private inline fun <reified T> Any?.cast() = this as T

