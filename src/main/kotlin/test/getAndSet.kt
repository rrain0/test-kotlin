package test.`get-and-set`

import java.util.concurrent.atomic.AtomicInteger

fun getAndSet(){
    val t = Test()
    println(t.v)
    println(t.v)
}

private class Test {
    companion object {
        val cnt = AtomicInteger()
    }

    // псевдосвойство, которое можно только брать (no backing field is created)
    val v get() = cnt.getAndIncrement()
    /*
        A backing field will be generated for a property if it uses the default implementation
        of at least one of the accessors,
        or if a custom accessor references it through the "field" identifier.
     */


    var v2 get() = cnt.getAndIncrement(); set(v2) = cnt.set(v2)

    // "field" is this field - provided automatically
    var v3 = 0; get() = field; set(v){ field = v*10 }

    var v4 = ""
        get() = "$field suffix"
        set(v){ field = "prefix $v" }




    // Backing properties
    private var _table: Map<String, Int>? = null
    public val table: Map<String, Int>
        get() {
            if (_table == null) {
                _table = HashMap() // Type parameters are inferred
            }
            return _table ?: throw AssertionError("Set to null by another thread")
        }
}