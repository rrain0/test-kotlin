package test.`map-to-object`



fun main(){
  mapToObjectByKotlin()
}




enum class SomeDataType { NORMAL, OTHER }
data class SomeData(val type: SomeDataType, val names: List<String>)
fun getOtherNamesListDistinct(someDatas: List<SomeData>): List<String> {
  return someDatas
    .filter { it.type==SomeDataType.NORMAL }
    .fold(mutableSetOf<String>()) { acc, data -> acc.addAll(data.names); acc }
    .toList()
}




/*
    The object is backed by map
    Changes in object reflects on map and vise versa
 */
fun mapToObjectByKotlin(){
  val pMap = mutableMapOf<String,Any?>("name" to "Person", "age" to 20)
  val p = Person2(pMap)
  
  // You can pass EMPTY map
  // You can set not existing properties
  // But you cannot get not existing property - NoSuchElementException
  val p2 = Person(mutableMapOf())
  p2.name = "lkj"

  val pp = PersonNullable(
    mutableMapOf<String, Any?>().withDefault { null } // no error when key not found
  )
  val name = pp.name


  println(p)
  pMap["age"] = 18
  println(p)
  p.age = 19
  println(pMap)

}

private class PersonVal(map: Map<String,Any?>){
  val name: String by map
  val age: Int by map
}
private class Person(map: MutableMap<String,Any?>){
  var name: String by map
  var age: Int by map
}
private class PersonNullable(map: MutableMap<String,Any?>){
  var name: String? by map
  var age: Int? by map
}


private class Person2(map: MutableMap<String,Any?>){
  var name: String by map
  var age: Int by map

  constructor(name: String, age: Int) : this(mutableMapOf("name" to name, "age" to age))

  override fun toString(): String {
    return "Person2(name='$name', age=$age)"
  }


}




