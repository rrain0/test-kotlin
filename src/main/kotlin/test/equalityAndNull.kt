
package test

fun equality() {
    val ptZero = Point(0.0, 0.0, "zero")
    val ptZero2 = ptZero

    // === - сравнение по ссылке
    println("ptZero === ptZero2:")
    println(ptZero === ptZero2) // => true

    // == - сравнение через equals
}


private class Point(var x:Double, var y:Double, var name:String) {

}


fun nulls(){

    fun getNullableList(): List<String>? = listOf("apple")

    // если полученное значение==null, то вернётся дефолтное, стоящее после ?:
    var list = getNullableList() ?: emptyList()


    var list2 = getNullableList()
    // ?. - проверка, что не null
    list2?.toSet() // если != null, тогда функция на объекте выполнится
    // !! - утверждение, что не null
    list2!!.toSet() // сказать компилятору, что здесь точно не null, но функция попытается выполниться, даже если там null


}