package test

import geometry.Point2
import geometry.Rectangle

fun classes(){
    val square = Rectangle(Point2(0.0,0.0), Point2(0.0,1.0), Point2(1.0,1.0), "square")



    // data class test:
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

}

private class Test2 {

    // Ленивая ининциализация
    val prop1: String by lazy { getProp1() }
    val prop1v2: String by lazy(::getProp1)

    // Отложенная инициализация
    // Если попытаться получить доступ раньше, чем переменная получит значение, то будет UninitializedPropertyAccessException
    // Переменная обязательно должна быть изменяемой (var),
    // не должна относиться к примитивным типам (Int, Double, Float и т.д),
    // не должна иметь собственных геттеров/сеттеров.
    lateinit var prop2: String

    // блок кода ининциализации - имеет данные из первичного конструктора
    init {
        if (::prop2.isInitialized) println("prop2 is initialized")
        else println("prop2 is NOT initialized")


    }

    // for "static" objects
    companion object {
        fun getProp1() = "some name"
    }

    init {

    }

}

// Constructors
private open class Test4(val a: Int, var b: Int, c: Int) {
    val d: Int
    var e: Int = c
    var f: Int? = null

    init {
        d = c
    }

    constructor(a: Int) : this(a, 0, 0)

    protected constructor(a: Int, b: Int) : this(a,b,0){
        this.b = a+b
        // f = b+this.c // can't access this.c
        f = b+0
    }

}

private fun Test4() = Test4(45, 65, 44)

private fun test(){
    var t4: Test4? = null
    t4 = Test4()
    t4 = Test4(5)
    t4 = Test4(5,6,6)
}


// private main constructor
private open class Test5 private constructor(val a: Int)



// classes are final by default, use open or sealed to make it inheritable
private open class A1(val name: String)

// Наследование и реализация интерфейсов через двоеточие
// наследоваться можно только от одного класса и от скольких угодно интерфейсов
private class B1(val cnt: Int, name: String) : A1(name), Comparable<B1> {

    override fun compareTo(other: B1): Int {
        return cnt - other.cnt
    }

}

// All direct subclasses of a sealed class/interface are known at compile time.
// Sealed class is abstract.
// Constructors can be protected (by default) or private
private sealed class C1
private class C2 : C1()




// abstract classes cannot be instantiated and can have abstract members
// abstract classes open by default
private abstract class D1{
    abstract val prop: String
    abstract fun f()
}
private class E1 : D1() {
    override val prop: String = ""
    override fun f(){}
}
private open class E2 : D1() {
    override val prop: String = ""
    // use final with overrided members to prohibit their override
    final override fun f(){}
}



// if no primary constructor in derived class, then you can call different constructors of base class
private open class BaseA {
    constructor(s1: String)
    constructor(s1: String, s2: String)
}
private class DerivedA : BaseA {
    constructor(s1: String) : super(s1)
    constructor(s1: String, s2: String) : super(s1,s2)
}





/*
    Visibility:
    ● private - the member is visible inside this class/file only (including all its members).
    ● protected - private visibility + visible in subclasses.
    ● internal - module visibility - any client inside this module who sees the declaring class sees its internal members.
    ● public (by default) - any client who sees the declaring class sees its public members.

    The overriding member will have the same visibility as the original.

    Local variables, functions, and classes can't have visibility modifiers.
 */

/*
    MODULES

    The internal visibility modifier means that the member is visible within the same module.
    More specifically, a module is a set of Kotlin files compiled together, for example:
    ● An IntelliJ IDEA module.
    ● A Maven project.
    ● A Gradle source set (with the exception that the test source set can access the internal declarations of main).
    ● A set of files compiled with one invocation of the <kotlinc> Ant task.
 */




