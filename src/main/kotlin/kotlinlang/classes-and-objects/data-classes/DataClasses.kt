package kotlinlang.`classes-and-objects`.`data-classes`



fun main() {
  dataClasses()
}


// Data class
// Autogeneration of equals(), hashCode(), toString(), componentN(), copy() for primary constructor props.
// copy() copies only primary constructor values.
// Data class is always final, cannot be abstract, open, sealed, inner.
// Data class can extend other classes and implement interfaces.
// Primary constructor allows only val / var prop-parameters and you can use default values.
// Getters / Setters / Props delegation are forbidden for primary constructor params.
// Primary constructor must have at least 1 param.


fun dataClasses() {
  val numbers = FourNumbers(5, 10)
  val (a, b) = numbers // component1 (val a) & component2 (var b)
  
  
  var copied = numbers.copy()
  copied.e = 100
  copied = copied.copy(b = 20)
  println("copied: $copied")
  println("copied.e: ${copied.e}")
  
  
  val fourNumbers1 = FourNumbers(4,5)
  val fourNumbers2 = FourNumbers(4,5)
  val fourNumbers3 = FourNumbers(4,10)
  println(fourNumbers1 == fourNumbers2) // true
  println(fourNumbers1 == fourNumbers3) // false
  
  fourNumbers2.d = 90
  println(fourNumbers1 == fourNumbers2) // true
  fourNumbers2.b = 91
  println(fourNumbers1 == fourNumbers2) // false
  
  println(fourNumbers2) // FourNumbers(a = 4, b = 91)
  
  
  // Standard data classes: Pair & Triple
  val pair = Pair("1", 1)
  val triple = Triple("1", 1, true)
}


private open class AA {
  var e : Int = 9
}
private data class FourNumbers(val a: Int, var b: Int) : AA() {
  val c: Int = 0
  var d: Int = 0
}


/*
    Additionally, the generation of data class members
    follows these rules with regard to the members’ inheritance:

    ● If there are explicit implementations of equals(), hashCode(), or toString()
        in the data class body or final implementations in a superclass,
        then these functions are not generated, and the existing implementations are used.
    ● If a supertype has componentN() functions that are open and return compatible types,
        the corresponding functions are generated for the data class and override those of the supertype.
        If the functions of the supertype cannot be overridden due to incompatible signatures
        or due to their being final, an error is reported.
    ● Providing explicit implementations for the componentN() and copy() functions is not allowed.
 */


