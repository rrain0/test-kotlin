package test

fun main(){
    whenAndIf()
}

private fun whenAndIf(){


    fun max1(a:Int, b:Int): Int {
        if (a>b){
            return a
        } else {
            return b
        }
    }
    // if is statement that returns value (instead of ternary: <> ? <> : <>)
    fun max2(a:Int, b:Int) = if (a>b) a else b



    println(":: WHEN STATEMENT ::")

    // when accepts single expressions or code blocks
    val str = getString()
    when (str){
        "1" -> println("1")
        "2" -> {
            println()
            println("2")
        }
        "3","4" -> println(str+"kf")
        else -> println(str)
    }

    // when returns value
    val int = getInt()
    val newInt = when(int){
        123,456 -> 500
        in -10..-5 -> 90
        else -> 0
    }


    run {
        // you can check one of specified conditions, separated by comma:
        // is it value or result of expression. Enum value without class name not supported even if it is know enum type.
        // is it instance of something (negation "!is" supported) or is something
        // is it in range (negation "!in" supported)
        var obj = getObjInt()
        when(obj){
            !in setOf(1,2,3,4,5,6,7,8,9,10,11) -> println("this is NOT 1,2,3,4,5,6,7,8,9,10,11")
            10 -> println()
            SomeEnum.B -> {
                println()
                println("this is SomeEnum.B")
            }
            in -10..-5 -> println("number in range ${-10..-5}")
            is String -> println("this is string object")
            !in 0..10000 -> println("Int number NOT in range ${0..10000}")
            1,2,3+6,getInt(),is Byte -> println("this is 1 or 2 or 3 or Byte")
            is Int, is Double -> println("this is number (Int or Double) object")
            Double -> println("this is Double companion object")
            Double::class -> println("this is Double class")
            null -> println("this is null")
            !is Short -> println("not Short")
            else -> println("unknown type")
        }

        /*var int = getInt()
        when(int){
            (int % 15 == 0) -> println("")
        }

        if (obj!=null) {
            println(obj::class.qualifiedName)
            println(obj is Int)
            println(obj is Int && obj % 15 == 0)
        }
        when(obj){
            (obj is Int && obj % 15 == 0) -> println("obj is Int and mod 15") // NOT WORKING
            else -> println("else")
        }*/
    }


}

private enum class SomeEnum { A, B, C }

private class Test{
companion object{
fun printSome() = println("some")
}
}



private fun getString() = "str"
private fun getInt() = 63

private fun getObjNull():Any? = null
private fun getObjByte():Any? = 67.toByte()
private fun getObjTwo():Any? = 2
private fun getObjInt():Any? = 15
private fun getObjDouble():Any? = 56.0
private fun getObjString():Any? = "56.0"
private fun getObjDoubleCompanionObject():Any? = Double
private fun getObjDoubleClass():Any? = Double::class
private fun getObjLong():Any? = 10L
private fun getObjSomeEnumB():Any? = SomeEnum.B