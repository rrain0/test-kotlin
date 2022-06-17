package test.objects

// object can't have constructor

// If you return an object, then its type is Any.
// You can name object or specify one of its supertypes as return type

// Named object
private object SingletonObject { // name is required in object declaration
    val name = "Single"
    var cnt = 8 as Int?
    var tag: String? = "first"
}

private val second = object {
    var cnt = 10
    var tag = "second" as String?
}

private open class AAA {}
private interface III {}
private object Object1 : AAA() {} // can inherit from classes
private val Object2 = object : III {} // can implement interfaces

//private val third = object Third { } // name not allowed in object expression

// the object type can be an interface
private interface TypeForObject {
    val x: Int
    val s: String
}

fun objects(){
    SingletonObject.cnt = 9
    second.cnt++


    fun getSingleton(): SingletonObject = SingletonObject

    fun getSecond(): Any = second

    fun getTypedObject(): TypeForObject = object : TypeForObject {
        override val x = 10
        override val s: String get() = "$x strings"
    }

}



private class Test{

    // companion objects can extend or implement something
    companion object : Comparable<Int>{
        // use @JvmStatic to make this member of companion object compiled as static
        //@JvmStatic
        val i = 10
        override fun compareTo(other: Int) = i.compareTo(other)
    }



}
// you can access companion object 2 ways:
private var testCompanionObject = Test.Companion
private var testCompanionObject2 = Test