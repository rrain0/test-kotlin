package test


operator fun String.inc() = this+" "
infix fun String.sp(s:String) = this+" "+s

fun stringOperators(){
    var s = "str"
    s++
    println(s) // "str "
    ++s
    println(s) // "str  "

    println("dksf" sp "dfk") // "dksf dfk"

    val s1 = "1"
    val s2 = "2"
    println(s1 sp s2) // "1 2"
}