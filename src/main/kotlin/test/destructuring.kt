package test.destructuring


fun destructuring(){

    // * - destructuring into vararg
    fun funVararg(vararg strings: String) { }
    funVararg("a", "str")
    funVararg(*arrayOf("a", "str"))
    funVararg(strings = *arrayOf("a", "str"))


    // var/val (<variables>) = something that have component[1,2,3...]() functions
    // only with var/val
    fun getPair() = 5 to "5"
    var (key,value) = getPair()
    val (key2,value2) = 6 to "6"
    var (a,b,c,d) = listOf(1,2,3,4,5)
    println("a=$a b=$b c=$c d=$d")

    // error: Destructuring declaration initializer of type List<Int> must have a 'component6()' function
    //var (a,b,c,d,e,f) = listOf(1,2,3,4,5,6)

    val firstComponent = listOf(1,2,3,4,5).component1()





    // destructuring in for loops
    for((key,value) in mapOf("1" to 1));
    /*
        To do so, you must implement operators:
            operator fun <K, V> Map<K, V>.iterator(): Iterator<Map.Entry<K, V>> = entrySet().iterator()
            operator fun <K, V> Map.Entry<K, V>.component1() = getKey()
            operator fun <K, V> Map.Entry<K, V>.component2() = getValue()
     */

}