package test.`classes-and-objects`

import kotlin.math.max


class OuterClass(
  var a: Int,
  var b: Int,
  var c: Int
) {
  var d = a+b
  var e = a+b+c
  var f = max(a,b)

  init {
    val inner = InnerClass()
  }

  inner class InnerClass{
    fun someFun(){
      val outer: OuterClass = this@OuterClass
      val outerF = outer.f
    }
  }

}

fun makeOuterAndInner(){
  val outer = OuterClass(1,2,3)
  val inner1 = outer.InnerClass()
  val inner2 = outer.InnerClass()
}