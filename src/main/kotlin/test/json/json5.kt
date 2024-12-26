package test.json

import kotlinx.serialization.*
import io.github.xn32.json5k.Json5
import io.github.xn32.json5k.SerialComment


// https://json5.org/
// https://github.com/Kotlin/kotlinx.serialization
// https://github.com/xn32/json5k



fun main(){
  //jsonTest()
  json5Test()
}


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

private data class Polygon2(

  val id: String,

  @SerialComment("Список координат вершин [x,y]")
  val vertices: List<List<Double>>,

  val color: String
)