package util

import java.util.*


fun main(){
  //pipeMapTest()
}



inline fun <reified T> Any?.cast(): T = this as T


infix fun String.dot(other: String) = this+"."+other

fun println(vararg args: Any?) = kotlin.io.println(args.joinToString(" "))

fun String.toBase64() = this.toByteArray().let { Base64.getEncoder().encodeToString(it) }

fun bool(value: Any?): Boolean {
  if (value == null) return false
  if (value == "") return false
  if (value == false) return false
  if (value == 0) return false
  if (value == 0L) return false
  if (value == 0.0) return false
  if (value == 0f) return false
  return true
}
fun Any?.toBool() = bool(this)
val Any?.bool get() = bool(this)

// better use (value ?: defaultValue)
fun <T : Any?>T.mapNull(block: () -> T & Any): T & Any {
  if (this == null) return block()
  return this
}



fun f(){
  val list = listOf("a","b","c").map { it + "1" }
  
}


fun ff(param1: String = "dsflj", block: ()->Unit){}

fun fff(){
  ff("dfj", {})
  ff("dsfj") {}
  ff {}
}



/* NOT NECESSARY TO MAKE SEPARATE FUNCTION FOR THIS
inline fun <T : Any> Any?.tryor(defaultValue: T, block: ()->T): T {
    return try { block() ?: defaultValue } catch (_: Throwable) { defaultValue }
}
*/




/* BETTER USE "WHEN" STATEMENT
And this doesn't compile because of null treatment

data class PipeMapResult<T : Any?, R : Any?>(
  var obj: T,
  var result: R? = null,
  var resultIsPresent: Boolean = false
){
  constructor(obj: T, result: R) : this(obj, result, true)

  inline fun mapTrue(action: T.()->R): PipeMapResult<T,R> {
    if (this.obj==true) return this.apply {
      result = obj.action()
      resultIsPresent = true
    }
    return this
  }
  inline fun mapFalse(action: T.()->R): PipeMapResult<T,R> {
    if (this.obj==false) return this.apply {
      result = obj.action()
      resultIsPresent = true
    }
    return this
  }
  inline fun elseGet(action: T.()->R): R {
    if (resultIsPresent) {
      //println("isPresent: $result")
      return result
    }
    return this.obj.action()
  }
}


inline fun <T : Any?, R : Any?> T.pipeTrue(action: T.()->R): PipeMapResult<T,R> {
  if (this==true) return PipeMapResult(this,this.action())
  return PipeMapResult(this)
}
inline fun <T : Any?, R : Any?> T.pipeTrueNull(action: T.()->R): PipeMapResult<T,R?> {
  return pipeTrue(action)
}

inline fun <T : Any?, R : Any?> T.pipeFalse(action: T.()->R): PipeMapResult<T,R> {
  // doesn't work
  if (this==false) return PipeMapResult(this,this.action())
  return PipeMapResult(this)
}
inline fun <T : Any?, R : Any?> T.pipeFalseNull(action: T.()->R): PipeMapResult<T,R?> {
  return pipeFalse(action)
}


fun pipeMapTest(){
  var firstValue: Boolean? = true
  var secondValue: Boolean? = true
  var thirdValue: Boolean? = null

  var firstMapped = firstValue.pipeTrue { "this is true" }
    .mapFalse { "this is false" }
    .elseGet { "not a Boolean" }
  var secondMapped = secondValue.pipeTrueNull { 0 }.elseGet { null }
  var secondMapped2 = secondValue.pipeTrueNull { null }.elseGet { null }
  var thirdMapped = thirdValue.pipeTrueNull { 0 }.elseGet { null }
  var thirdMapped2 = thirdValue.pipeTrueNull { null }.elseGet { null }

  var firstMapped2 = if (firstValue==true) "this is true"
  else if (firstValue==false) "this is fasle"
  else ""

  var firstMapped3 = firstValue.let {
    if (it==true) "this is true"
    else if (it==false) "this is fasle"
    else ""
  }

  println(firstMapped)
  println(secondMapped)
  println(secondMapped2)
  println(thirdMapped)
  println(thirdMapped2)
}


*/
