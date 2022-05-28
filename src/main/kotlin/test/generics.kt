package test

/*
    Simply <Type> is invariant

    Covariant type in Java: <? extends Type>:
    Collection<String> is a subtype of Collection<? extends Object>. In other words, the wildcard with an extends-bound (upper bound) makes the type covariant.

    Contravariant type in Java: <? super String>

    Joshua Bloch gives the name Producers to objects you only read from and Consumers to those you only write to. He recommends:
    "For maximum flexibility, use wildcard types on input parameters that represent producers or consumers", and proposes the following mnemonic:
    PECS stands for Producer-Extends, Consumer-Super.

    Declaration-site variance:
    Producers (only read from producer) in Kotlin represents as <out Type> - can only return (produce) Type
    Consumers (only write to consumer) in Kotlin represents as <in Type> - can only consume Type
 */



// Upper bound <Type : UpperBoundType> - Java <Type extends UpperBoundType>
// Default upper bound is Any?
private fun <T : Comparable<T>> sort(list: List<T>){}
private fun upperBoundDemo(){
    sort(listOf(1, 2, 3)) // OK. Int is a subtype of Comparable<Int>
    //sort(listOf(HashMap<Int, String>())) // Error: HashMap<Int, String> is not a subtype of Comparable<HashMap<Int, String>>
}
// More than one upper bound: where ...
private fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String>
        where T : CharSequence,
              T : Comparable<T> {
    return list.filter { it > threshold }.map { it.toString() }
}




// Covariance <out Type> - Java <? extends T>
private interface Source<out T> {
    fun next(): T
}
private fun covarianceDemo(strs: Source<String>) {
    val objects: Source<Any> = strs // This is OK, since T is an out-parameter
    // ...
}



// Contravariance <in Type>  - Java <? super T>
private interface ComparableMy<in T> {
    operator fun compareTo(other: T): Int
}
private fun contravarianceDemo(x: ComparableMy<Number>) {
    x.compareTo(1.0) // 1.0 has type Double, which is a subtype of Number
    // Thus, you can assign x to a variable of type Comparable<Double>
    val y: ComparableMy<Double> = x // OK!
}





// Star-projections <*> - you know nothing about type but still want to use it

// For Foo<out T : TUpper>
// Foo<*> is equivalent to Foo<out TUpper>
// This means that when the T is unknown you can safely read values of TUpper from Foo<*>

// For Foo<in T>
// Foo<*> is equivalent to Foo<in Nothing>.
// This means there is nothing you can write to Foo<*> in a safe way when T is unknown.

// For Foo<T : TUpper>, where T is an invariant type
// Foo<*> is equivalent to Foo<out TUpper> for reading values and to Foo<in Nothing> for writing values.



/*
    At runtime Generic types are erased
    So "foo as List<String>" at runtime only checks "foo as List<*>"
 */
// You can use "reified" in inline functions to access generic types at runtime
// Also there works "is" "!is" "as" operators with T
inline fun <reified T> getListGenericType(obj:List<T>) = println(T::class)
// getListGenericType(listOf<Number>(1,2)) prints "class java.lang.Number (Kotlin reflection is not available)"




