package test.json.jackson

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.core.json.JsonWriteFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper


fun createJacksonJson5Mapper(): ObjectMapper {
  
  // https://github.com/FasterXML/jackson-core/wiki/JsonReadFeatures
  val json5Mapper = JsonMapper.builder()
    
    .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
    .enable(JsonReadFeature.ALLOW_YAML_COMMENTS)
    .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
    .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
    .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
    .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
    .enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS)
    .enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS)
    .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS)
    .enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS)
    .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
    .enable(JsonReadFeature.ALLOW_MISSING_VALUES)
    .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
    
    // TODO enable single quote writing
    .disable(JsonWriteFeature.QUOTE_FIELD_NAMES) // TODO enable quotes for names with special characters
    .disable(JsonWriteFeature.WRITE_NAN_AS_STRINGS)
    
    .build()
  
  json5Mapper.configureJacksonPrettier()
  
  return json5Mapper
}


fun main() {
  val json5Mapper = createJacksonJson5Mapper()
  
  val tree = json5Mapper.readValue("""
    {
      prop: 'Juggernaut',
      map: { prop: 'aaa' },
      'title/title': 'Title',
    }
    """.trimIndent(),
    Map::class.java
  )
  
  println("deserialized: $tree")
  
  val string = json5Mapper.writeValueAsString(tree)
  println("serialized: $string")
}
