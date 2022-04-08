
package test

fun equality() {
    val ptZero = Point(0.0, 0.0, "zero")
    val ptZero2 = ptZero

    // сравнение по ссылке
    println("ptZero === ptZero2:")
    println(ptZero === ptZero2) // => true

}


private class Point(var x:Double, var y:Double, var name:String) {

}