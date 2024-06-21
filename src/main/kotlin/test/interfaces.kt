package test.interfaces


fun interfaces() {

}

private interface I {
  
  //constructor(a: Int) // constructors can't be in interfaces
  
  val a: Int
  var b: String?
  var c: Int
  
  // default realisation
  fun getAPlusC() = a + c
  
  fun getABCString(): String
}

private val objI: I = object : I {
  override val a = 0
  override var b = "" as String?
  override var c = 0
  
  override fun getABCString() = "$a $b $c"
}
