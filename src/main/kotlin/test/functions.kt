
package test

import kotlin.math.abs
import kotlin.math.cos

// type alias for function (nested and local type aliases not supported)
typealias EventHandler = (ev: Any) -> Unit


fun functions() {

    // Function parameters are read-only!!!

    run{
        // 2 способа написать обычную функцию
        fun fun1(): Int { return 1 }
        fun fun2() = 1 // Single-expression function

        fun fun3() { println() } // Unit return type (void in Java)
        fun fun4(): Unit { println() } // Unit return type (void in Java)
        fun fun5() = println() // Unit return type (void in Java)


        // Extension Functions - Функции-расширения - можно задать им внутренний this и потом вызывать их как будто они принадлежат объекту
        fun List<Any>?.printAll(maxLen:Int?){
            if (this == null) return
            for (i in 0..Math.min(this.lastIndex, (maxLen ?: 0)-1)){
                print(this[i])
                print(" ")
            }
            println()
        }

        val list = listOf(1, 4, 6, 9)
        list.printAll(3)
        null.printAll(9)


        // Trailing comma
        fun fun6(int:Int, str:String, ){ }

        // Default values
        fun read(b: ByteArray, off: Int = 0, len: Int = b.size, tag:String = "tag") { }
        read(ByteArray(10))
        read(ByteArray(10), 10)
        read(ByteArray(10), 10, 50)

        // Call function using named arguments - they must be after positioned arguments
        read(ByteArray(10), len = 9, tag = "")
        read(ByteArray(10), len = 9, off = 10)
        read(ByteArray(10), len = 9, tag = "", off = 10)

        // Varargs - vararg can be only one
        fun fun7(vararg strings: String) { }
        fun7("a", "str")
        fun7(*arrayOf("a", "str"))
        fun7(strings = *arrayOf("a", "str"))

        // b must be named argument
        fun fun8(a: String, vararg strings: String, b:String) { }
        fun8("a", "arg1", "arg2", b = "b")
        fun fun9(a: String, vararg strings: String, b:Int) { }
        fun9("a", "arg1", "arg2", b = 12)


        // * - Spread operator
        fun7(*arrayOf("a", "str"))
        fun <T> asList(vararg arrayElems: T): List<T> {
            return listOf(*arrayElems)
        }

        val intArr = intArrayOf(1, 2, 3) // IntArray is a primitive type array
        val arrOfInts = arrayOf(1, 2, 3) // Array<Int> is typed array
        fun asList2(vararg ints: Int){}
        asList2(*intArr)
        asList2(*arrOfInts.toIntArray())


        // Infix notation
        // They must be member or extension functions
        // They must have single parameter (and not vararg, and have no default value)
        infix fun Int.add(int: Int) = this+int
        8 add 9 // equivalent 8.add(9)
        8.add(9)
        // if use as member, you can call it: this infixFun param or infixFun(param)
        /*
            Infix priority:
            Infix function calls have lower precedence than arithmetic operators,
            type casts, and the rangeTo operator. The following expressions are equivalent:

            1 shl 2 + 3 is equivalent to 1 shl (2 + 3)
            0 until n * 2 is equivalent to 0 until (n * 2)
            xs union ys as Set<*> is equivalent to xs union (ys as Set<*>)

            On the other hand, an infix function call's precedence is higher than that of the
            boolean operators && and ||, is- and in-checks, and some other operators.
            These expressions are equivalent as well:

            a && b xor c is equivalent to a && (b xor c)
            a xor b in c is equivalent to (a xor b) in c
         */

    }



    // Function scope:
    // Local functions - functions inside functions
    // Member functions - defined inside class or object



    run {
        // Tail recursive functions
        // For some algorithms that would normally use loops,
        // you can use a recursive function instead WITHOUT the risk of STACK OVERFLOW.

        val eps = 1e-10

        tailrec fun findFixPoint(x: Double = 1.0): Double =
            if (abs(x - cos(x)) < eps) x else findFixPoint(cos(x))
    }

    run{
        // LAMBDA & Anonymous Function


        // Lambda parameter
        fun fun10(a:Int, l: ()->Unit, b:String) { }
        // { } - lambda literal
        fun10(1, { }, "b")
        // if lambda the last
        fun fun11(a:Int, vararg b:String, l: ()->Unit) { }
        fun11(1, "1", "2", "3") { println("i am the last parameter lambda!") }


        fun <T,R> Collection<T>.reduce( initial: R, combine: (R,T)->R ): R { // or you can name args: combine: (accumulator: R, nextElem: T)->T
            var accumulator = initial
            for (elem in this){
                accumulator = combine(accumulator, elem)
            }
            return accumulator
        }

        val list = listOf(1, 4, 6)
        var sum: Int
        // ссылка на функцию
        sum = list.reduce(0, Int::plus)
        // { } - lambda literal
        sum = list.reduce(0, { acc:Int, elem:Int -> acc + elem})
        // lambda types are inferred
        sum = list.reduce(0, { acc, elem ->
            println(acc)
            acc + elem // last expression in lambda is considered as return value
        })
        // if lambda the last, you can write it right after function call
        sum = list.reduce(0) { acc, elem -> acc + elem}
        // anonymous function - you can omit contextual parameter types and you can declare return type (in lambda you can't)
        sum = list.reduce(0, fun(acc, elem:Int):Int { return acc + elem } )

        // Создание реализации функционального интерфейса как переменной
        val runnable: Runnable = Runnable { /*code*/ }


        // ССЫЛКА НА ФУНКЦИЮ
        /*
            Use a callable reference to an existing declaration:
            ● a top-level, local, member, or extension function: ::isOdd, String::toInt,
            ● a top-level, member, or extension property: List<Int>::size,
            ● a constructor: ::Regex
            ● These include bound callable references that point to a member of a particular instance: foo::toString, this::toString, ::toString.
         */

        class ToRun : Runnable {
            override fun run(){
                Thread(::start).start()
                Thread(this::start).start()
                Thread { this.start() }.start()
                Thread({ this.start() }).start()
                Thread(fun(){ this.start() }).start()
            }

            private fun start(){

            }
        }

        run {
            var zeroToUnitLambda: ()->Unit = {}
            var oneToUnitLambda: (arg1: Int)->Unit = {}
        }

        run{
            fun f(f:(Int,String)->String) = println(f(23,"aaa"))
            fun ff(f:Int.(String)->String) = println(f(23,"aaa"))

            fun f1(i:Int, s:String) = "$i $s"
            fun Int.f2(s:String) = "$this $s"

            f(::f1) // для ::f1 this просто не существует
            f(Int::f2) // если бы ты писал вызов f внутри класса Int, то вместо Int::f2 можно бы было this::f2 или просто ::f2

            ff(::f1)
            ff(Int::f2)

            val a = fun Int.(){}
            5.a()

            /*
                f2 как функция расширение для класса Int, можешь считать, что она внутри него объявлена
                для f2 первый аргумент - это this типа Int, второй, который явно объявлен в скобках s типа String
                у f1 нету this, так что аргументы начинаются сразу  с объявленных в скобках
                в Java кстати такое тоже точно так же работает
                только там нельзя опустить this
                в общем и целом двойное двоеточие - это ссылка на существующую функцию)
             */
        }



        // Function types:
        // () -> Unit
        // () -> String
        // (A,B) -> C
        // (x: Int, y: Int) -> Point // optionally you can name function parameters
        // A.(B) -> C // extension function type
        // ((Int,Int) -> Int)? // nullable function
        // (Int) -> ((Int) -> Unit) /*equivalent to*/  (Int) -> (Int) -> Unit // function returns function (The arrow notation is right-associative)
        // suspend () -> Unit // suspending function

        // Functional Interface
        // IntTransformer inherits function (Int) -> Int
        class IntTransformer: (Int) -> Int {
            override operator fun invoke(x: Int): Int = x+10
        }
        val intFun: (Int) -> Int = IntTransformer()
        // if lambda has only one parameter, it implicitly named "it"
        val intFun2: (Int) -> Int = { it*2 }


        // Non-literal values of function types with and without a receiver are interchangeable
        // (A,B) -> C same as A.(B) -> C when assign them as stored in variables (not literals)
        var repeatFun1: String.(Int) -> String = { times -> this.repeat(times) }
        // repeat() is implicit this.repeat()
        var repeatFun1_2: String.(Int) -> String = { times -> repeat(times) }
        //var repeatFun2: (String,Int) -> String = { times -> this.repeat(times) } // not allowed
        var repeatFun3: (String,Int) -> String = { str, times -> str.repeat(times) }
        //var repeatFun4: (String,Int) -> String = { times -> this.repeat(times) } // not allowed

        var repeatFun5: String.(Int) -> String = repeatFun3
        var repeatFun6: (String,Int) -> String = repeatFun1


        // Invoking functions
        repeatFun1.invoke("aa",8)
        "aa".repeatFun1(8)


        // Explicitly return from lambda using labelled return@label
        // "return" keyword returns from the nearest function declared with "fun" keyword
        val strings = listOf("sfdsf", "fdkslf")
        strings.filter { it.length == 5 }.sortedBy { it }.map { it.uppercase() }
        // implicit labels is function names in which you passed lambda
        // explicit label is label@{ }
        strings.filter { return@filter it.length == 5 }.sortedBy { return@sortedBy it }.map mapLambda@{ return@mapLambda it.uppercase() }


        // _ - unused lambda variables marks by underscore
        mapOf("fff" to 1).forEach { _,value -> println(value) }


        // you can modify outer variables in lambdas
        var sum2 = 0
        listOf(1, 2, 3).filter { it > 0 }.forEach {
            sum2 += it
        }
        print(sum2)
    }
}


/*
    Content of inline functions is inlined to call sites.
    "inline" makes that, so when you call function from parameter,
    compiler doesn't create a function object and copies code from function into body of this function.
    So you can't store inlined functions in variables because they are not objects yet.

    Inlining cause generated code grow.
    Inlining speed up program because no need Function object.
    You can reify generic type parameters with fun <reified T> (){}
 */
private inline fun inlineFun(inlined: () -> Unit, noinline notInlined: () -> Unit) {
    inlined()
    notInlined()
    //val inlF = inlined // error - function already not an object and it is simply code that copied into this function instead of call statement
    val notinlF = notInlined
}


/*
    Crossinline.
    Note that some inline functions may call the lambdas passed to them as parameters not directly from the function body,
    but from another execution context, such as a local object or a nested function.
    In such cases, non-local control flow is also not allowed in the lambdas.
    To indicate that the lambda parameter of the inline function cannot use non-local returns,
    mark the lambda parameter with the crossinline modifier:
 */
private inline fun fInline(crossinline body: () -> Unit) {
    val f = object: Runnable {
        override fun run() = body()
    }
    // ...
}