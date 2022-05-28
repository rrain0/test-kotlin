package test


operator fun String.inc() = this+" "
infix fun String.sp(s:String) = this+" "+s

fun strings(){
    run {
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


    run {
        // Build a string:
        val countDown = buildString {
            for (i in 5 downTo 1) {
                append(i)
                appendLine()
            }
        }
    }


    run {
        // string templates

        val s = "str"
        val s1 = "1"

        // single variable
        val ss = "sdklfjdlksj $s $s1"
        // expression
        val ss2 = "sldjflskdjf ${s.substring(0)} sdlkjf ${s1.substring(1)}"
    }

    run {
        // text blocks / multiline strings
        println("MULTILINE STRINGS:")

        var s:String

        s = """
            Kotlin
            Java
        """.trimIndent()
        println(s)

        s = """
              Kotlin
            Java
        """.trimIndent() // makes 2 spaces before Kotlin
        println(s)

        s = """
            #  Kotlin
            #  Java
        """.trimMargin("#")
        println(s)

        s = """
         #  Kotlin
            #  Java
        """.trimMargin("#") // trim all before "#" inclusive
        println(s)

        s = """
            
        """.trimIndent()
    }


}