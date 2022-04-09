package test


operator fun String.inc() = this+" "
infix fun String.sp(s:String) = this+" "+s

fun strings(){
    var s = "str"
    s++
    println(s) // "str "
    ++s
    println(s) // "str  "

    println("dksf" sp "dfk") // "dksf dfk"

    val s1 = "1"
    val s2 = "2"
    println(s1 sp s2) // "1 2"


    // string templates
    // single variable
    val ss = "sdklfjdlksj $s $s1"
    // expression
    val ss2 = "sldjflskdjf ${s.substring(1)} sdlkjf ${s1.substring(9)}"
}