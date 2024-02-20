package parser






fun main(){
  run {
    val expression = "1123+878+(123/8*9-0)"
    println(expression)
    println(expression.lexify().joinToString(separator = "\n") { it.toString() })
  }
  run {
    val expression = "-12.34+.878+(123./8*9-0.1)"
    println(expression)
    println(expression.lexify().joinToString(separator = "\n") { it.toString() })
  }
  run {
    val expression = "sin(12.3)+si(7)"
    println(expression)
    println(expression.lexify().joinToString(separator = "\n") { it.toString() })
  }
}