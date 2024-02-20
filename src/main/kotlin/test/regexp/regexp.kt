package test.regexp





fun durationPattern(){
  val durationPattern = Regex("""((?<h>\d+)h)?((?<m>\d+)m)?((?<s>\d+)s)?""")
  val match = durationPattern.matchEntire("1h46m")
  val h = match?.groups?.get("h")?.value
  val m = match?.groups?.get("m")?.value
  val s = match?.groups?.get("s")?.value
  println("h: $h, m: $m, s: $s")
}


fun ipPattern(){
    
    
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
    println(matchResult==null)
    
    
}


fun snakeCaseToCamelCase(){
  val pattern = Regex("""_[\da-zA-Z]""")
  println("place_sub_type_0a: ${pattern.replace("place_sub_type_0a",{ mr -> mr.value[1].uppercase() })}")
}

