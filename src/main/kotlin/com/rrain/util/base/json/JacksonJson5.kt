package com.rrain.util.base.json

import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.core.json.JsonWriteFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper


fun createJacksonJson5Mapper(): ObjectMapper {
  return JsonMapper().configureJson5()
}


fun ObjectMapper.configureJson5(): ObjectMapper {
  configureJsonTypes()
  configureJsonPrettier()
  configureJson5Prettier()
  return this
}


fun ObjectMapper.configureJson5Prettier(): ObjectMapper {
  // https://github.com/FasterXML/jackson-core/wiki/JsonReadFeatures
  enable(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature())
  enable(JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature())
  enable(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature())
  enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES.mappedFeature())
  enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
  enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature())
  enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature())
  enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS.mappedFeature())
  enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature())
  enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature())
  enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature())
  enable(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature())
  enable(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature())
  
  // https://github.com/FasterXML/jackson-core/wiki/JsonWriteFeatures
  // TODO enable single quote writing
  // TODO enable quotes for names with special characters
  disable(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature())
  disable(JsonWriteFeature.WRITE_NAN_AS_STRINGS.mappedFeature())
  
  return this
}


// Test Jackson Json5
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
