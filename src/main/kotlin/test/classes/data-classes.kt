package test.classes.`data-classes`



// data class - автогенерация equals(), hashCode(), toString(), componentN(), copy()
// data class is always final, cannot be abstract, open, sealed, inner
// data class can extend other classes and implement interfaces
// В первичном конструкторе нет просто параметров, только объявляются переменные (val / var) и можно указать дефолтные значения
// Первичный конструктор должен иметь хотя бы 1 параметр

// equals(), hashCode(), toString(), componentN() - только для переменных первичного конструктора
// copy() - Copies all properties

fun dataClasses(){
    val fn = FourNumbers(5, 10)
    val (a,b) = fn // component1 (val a) & component2 (var b)

    var fnCopied = fn.copy()
    fnCopied = fn.copy(b=20)


    val fourNumbers1 = FourNumbers(4,5)
    val fourNumbers2 = FourNumbers(4,5)
    val fourNumbers3 = FourNumbers(4,10)
    println(fourNumbers1 == fourNumbers2) // true
    println(fourNumbers1 == fourNumbers3) // false

    fourNumbers2.d = 90
    println(fourNumbers1 == fourNumbers2) // true
    fourNumbers2.b = 91
    println(fourNumbers1 == fourNumbers2) // false

    println(fourNumbers2) // FourNumbers(a=4, b=91)


    // Standard data classes: Pair & Triple
    val pair = Pair("1",1)
    val triple = Triple("1",1,true)
}


private data class FourNumbers(val a: Int, var b: Int) : AA() {
    val c: Int = 0
    var d: Int = 0
}
private open class AA{
    var e : Int = 9
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


