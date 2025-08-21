package kotlinlang.regexp


// В котлине у самой регулярки нет понятия последненго сохранённого индекса символа,
// тут надо брать матч

fun main() {
  testIfEntireStringMatches()
}


fun testIfEntireStringMatches() {
  val r = Regex("""\d{2,4}""")
  println(r.matches("a123")) // false
  println(r.matches("123")) // true
  println(r.matches("1")) // false
}


fun caseConvert() {
  run {
    val snakeCase = "place_sub_type_0123a"
    val camelCase = snakeCase.snakeCaseToCamelCase() // => placeSubType0a
    println("snake case to camel case: $snakeCase -> $camelCase")
  }
  run {
    val camelCase = "placeSubType0123aHTMLanguage"
    val kebabCase = camelCase.camelCaseToKebabCase() // => place-sub-type0a
    println("camel case to kebab case: $camelCase -> $kebabCase")
  }
  run {
    val camelCase = "placeSubType0123aHTMLanguage"
    val words = camelCase.camelCaseToWords()
    println("camel case to words: $camelCase -> $words")
  }
}



fun durationPattern() {
  val durationPattern = Regex("""((?<h>\d+)h)?((?<m>\d+)m)?((?<s>\d+)s)?""")
  val match = durationPattern.matchEntire("1h46m")
  val h = match?.groups?.get("h")?.value
  val m = match?.groups?.get("m")?.value
  val s = match?.groups?.get("s")?.value
  println("h: $h, m: $m, s: $s")
}


fun ipPattern() {
    
    
    var someIp = "192.198.3.5"
    
    val ipPattern = Regex("""(?<n1>\d{1,3})\.(?<n2>\d{1,3})\.(?<n3>\d{1,3})\.(?<n4>\d{1,3})""")
    
    var matchResult = ipPattern.matchEntire(someIp)
    if (matchResult != null){
        val matchGroupsMap = mapOf(
            "n1" to matchResult.groups["n1"],
            "n2" to matchResult.groups["n2"],
            "n3" to matchResult.groups["n3"],
            "n4" to matchResult.groups["n4"],
        )
        
        println(matchGroupsMap)
        
        
        val matchGroupValuesMap = mapOf(
            "n1" to matchResult.groups["n1"]!!.value,
            "n2" to matchResult.groups["n2"]!!.value,
            "n3" to matchResult.groups["n3"]!!.value,
            "n4" to matchResult.groups["n4"]!!.value,
        )
        
        println(matchGroupValuesMap)
    }
    
    
    someIp = "192.198.*.5"
    matchResult = ipPattern.matchEntire(someIp)
    println(matchResult == null)
    
    
}


fun String.snakeCaseToCamelCase(): String {
  val pattern = Regex("""_[^_]""")
  return pattern.replace(this, { mr -> mr.value[1].uppercase() })
}

fun String.camelCaseToKebabCase(): String {
  // \p{Lu} - match letter uppercase, \d+ - match 1+ digit
  val pattern = Regex("""\p{Lu}|\d+""")
  return pattern.replace(this, { mr -> "-${mr.value.lowercase()}" })
}

fun String.camelCaseToWords(): List<String> {
  val pattern = Regex("""(?<=\p{Ll}|\p{Lu})(?=\p{Lu}|\d+)""")
  return this.split(pattern)
}


private fun usingGroups() {
  val regex = """\b(?<city>[A-Za-z\s]+),\s(?<state>[A-Z]{2}):\s(?<areaCode>[0-9]{3})\b""".toRegex()
  val input = "Coordinates: Austin, TX: 123"
  
  val match = regex.find(input)!!
  println(match.groups["city"]?.value)
  // Austin
  println(match.groups["state"]?.value)
  // TX
  println(match.groups["areaCode"]?.value)
  // 123
}
