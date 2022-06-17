
package test.equality


fun main(){
    //equality()
    //nulls()
    floatNumberEquality()
}


private fun equality() {
    val ptZero = Point(0.0, 0.0, "zero")
    val ptZero2 = ptZero
    val ptZero3 = Point(0.0, 0.0, "zero3")
    val ptOne = Point(1.0, 1.0, "one")


    // === - сравнение по ссылке
    println("ptZero === ptZero2: ${ptZero === ptZero2}") // => true
    println("ptZero === ptZero3: ${ptZero === ptZero3}") // => false
    println("ptZero === ptOne: ${ptZero === ptOne}") // => false


    // == - сравнение через equals
    println("ptZero == ptZero2: ${ptZero == ptZero2}") // => true
    println("ptZero == ptZero3: ${ptZero == ptZero3}") // => true
    println("ptZero == ptOne: ${ptZero == ptOne}") // => false

}
private class Point(var x:Double, var y:Double, var name:String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}

private fun nulls(){
    fun getNullableList(): List<String>? = listOf("apple")

    // если полученное значение==null, то вернётся дефолтное, стоящее после ?:
    var list = getNullableList() ?: emptyList()


    var list2 = getNullableList()
    // ?. - проверка, что не null
    list2?.toSet() // если != null, тогда функция на объекте выполнится
    // !! - утверждение, что не null
    list2!!.toSet() // сказать компилятору, что здесь точно не null, но функция попытается выполниться, даже если там null
}

private fun floatNumberEquality(){
    /*
        Это касается операций:
        ● Проверки на равенство: a == b, a != b
        ● Операторы сравнения: a < b, a > b, a <= b, a >= b
        ● Создание диапазона и проверка диапазона: a..b, x in a..b, x !in a..b

        Если компилятору статически понятно, что тут точно будет число (Float, Float?, Double, Double?),
        для операций сравнения используется стандарт IEEE 754 для арифметики с плавающей точкой.

        Если же не понятно, что там точно будет число (напр. тип Any, Comparable<...>),
        то используются реализации equals и compareTo для Float и Double, которые не согласуются со стандартом, так что:
        ● NaN считается равным самому себе
        ● NaN считается больше, чем любой другой элемент, включая "POSITIVE_INFINITY"
        ● -0.0 считается меньше, чем 0.0
     */


    infix fun Any?.isEq(other: Any?) = this==other

    println("Double.NaN == Double.NaN: ${Double.NaN == Double.NaN}") // false
    println("Double.NaN isEq Double.NaN: ${Double.NaN isEq Double.NaN}") // true
}