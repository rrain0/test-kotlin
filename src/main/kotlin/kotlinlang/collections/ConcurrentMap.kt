package kotlinlang.collections

import java.util.concurrent.ConcurrentHashMap



fun main() {
  val map: MutableMap<String, Int> = ConcurrentHashMap(mapOf(
    "key2" to 2,
    "key30" to 30,
    "key100" to 100,
  ))
  
  // Atomic getOrPut
  var valueByKey1 = map.getOrPut("key1", { 1 })
  valueByKey1 = map.getOrPut("key1", { 2 })
  println("valueByKey1: $valueByKey1") // valueByKey1: 1
  println("map: $map") // map: {key1=1, key2=2, key30=30, key100=100}
  
  // Удаляет значения из исходной мапы
  map.values.removeIf { it >= 10 }
  println("map: $map") // map: {key1=1, key2=2}
  
}