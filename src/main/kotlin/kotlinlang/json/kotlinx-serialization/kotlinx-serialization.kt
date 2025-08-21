package kotlinlang.json.`kotlinx-serialization`

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.util.*


fun main() {
  kotlinxJsonSerializationTest()
}


// Custom serializer for UUID type
private object UUIDSerializer : KSerializer<UUID> {
  
  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
  
  override fun deserialize(decoder: Decoder): UUID =
    UUID.fromString(decoder.decodeString())
  
  override fun serialize(encoder: Encoder, value: UUID) =
    encoder.encodeString(value.toString())
  
}



private fun kotlinxJsonSerializationTest(){
  
  run {
    // Serializing objects
    val coloredObject = ColoredObject(
      UUID.fromString("4b6f3a73-7ba3-4b10-a24d-18b1e815648f"),
      "square",
      4.56,
      "red"
    )
    val coloredObjectJson = Json.encodeToString(coloredObject)
    println("coloredObjectJson: $coloredObjectJson") // {"id":"4b6f3a73-7ba3-4b10-a24d-18b1e815648f","type":"square","size":4.56,"color":"red"}
    
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
    val testObjectDeserialized = Json { isLenient = true }.decodeFromString<TestObject>(testObjectJson)
    println("lenient testObjectDeserialized: $testObjectDeserialized")
  }
}

@Serializable
private data class ColoredObject(
  // apply our custom UUID serializer
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  val type: String,
  val size: Double,
  val color: String
)

@Serializable
private data class TestObject(
  // переименовать свойство в итоговом JSON
  @SerialName("boolProp")
  val booleanProperty: Boolean,
  
  // свойство с дефолтным значенем становится необязательным в JSON
  val optionalProp: Int? = null,
)