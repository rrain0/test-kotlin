
package test

fun exceptions() {

    fun fail(a:Int): Nothing {
        when(a){
            0 -> throw RuntimeException()
            1 -> throw IndexOutOfBoundsException()
            else -> throw Exception()
        }
    }

    // Multi catch is not allowed
    try {
        fail(0)
    } catch (e: RuntimeException){
        println("RuntimeException")
    } catch (e: IndexOutOfBoundsException){
        println("IndexOutOfBoundsException")
    } finally {
        println("finally")
    }

    // Try is an expression
    // parsed will be null
    var parsed:Int? = try { "asdfasd".toInt() } catch (e: NumberFormatException) { null }



    // The Nothing type

    fun fail(message: String): Nothing {
        throw IllegalArgumentException(message)
    }

    var list:List<Int>? = listOf(1,2)
    var len = list?.size ?: fail("fail") // Nothing can be anywhere because never reaches to assignment
}