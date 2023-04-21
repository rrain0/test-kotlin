package test

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import io.github.xn32.json5k.Json5
import io.github.xn32.json5k.SerialComment


// https://json5.org/
// https://github.com/Kotlin/kotlinx.serialization
// https://github.com/xn32/json5k



fun main(){
  //jsonTest()
  json5Test()
}

fun jsonTest(){

  run {
    // Serializing objects
    val coloredObject = ColoredObject("13", "square", 4.56, "red")
    var coloredObjectJson = Json.encodeToString(coloredObject)
    println("coloredObjectJson: $coloredObjectJson") // {"id":"13","type":"square","size":4.56,"color":"red"}

    // Deserializing objects
    val coloredObjectDeserialized = Json.decodeFromString<ColoredObject>(coloredObjectJson)
    println("coloredObjectDeserialized: $coloredObjectDeserialized")
  }

  run {
    val testObjectJson = """{
      "boolProp": true
    }""".trimMargin()
    val testObjectDeserialized = Json.decodeFromString<TestObject>(testObjectJson)
    println("testObjectDeserialized: $testObjectDeserialized")
  }

  run {
    val testObjectJson = """{
      boolProp: true
    }""".trimMargin()
    // isLenient allows to omit unnecessary quotes in field names
    val testObjectDeserialized = Json { isLenient=true }.decodeFromString<TestObject>(testObjectJson)
    println("lenient testObjectDeserialized: $testObjectDeserialized")
  }
}

@Serializable
private data class ColoredObject(
  val id: String,
  val type: String,
  val size: Double,
  val color: String
)

@Serializable data class TestObject(

  // переименовать свойство в итоговом JSON
  @SerialName("boolProp")
  val booleanProperty: Boolean,

  // свойство с дефолтным значенем становится необязательным в JSON
  val optionalProp: Int? = null,
)



fun json5Test(){
  run {

    val polygon2 = Polygon2(
      "48943",
      listOf( listOf(2.5,3.5), listOf(4.2,4.5), listOf(1.3,0.0) ),
      "red"
    )

    val polygon2Json = Json5 {
      prettyPrint = true // multiline & include @SerialComment
      indentationWidth = 2 // line indents
      useSingleQuotes = true // " -> '
      quoteMemberNames = false // no unnecessary quotes of field names
      encodeDefaults = true
    }.encodeToString(polygon2)

    println("polygon2Json: $polygon2Json")
    /* output:
      polygon2Json: {
        id: '48943',
        // Список координат вершин [x,y]
        vertices: [
          [
            2.5,
            3.5
          ], [
            4.2,
            4.5
          ], [
            1.3,
            0.0
          ]
        ],
        color: 'red'
      }
   */

  }

}

@Serializable
private data class Polygon2(

  val id: String,

  @SerialComment("Список координат вершин [x,y]")
  val vertices: List<List<Double>>,

  val color: String
)