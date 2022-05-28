package test.interfaces


fun interfaces(){

}

private interface I {

    //constructor(a: Int) // constructors can't be in interfaces

    val a: Int
    var b: String?
    var c: Int

    // default realisation
    fun getAPlusC() = a+c

    fun getABCString(): String
}

