package parser




class LexemeChar(
  var char: Char?,
  var next: MutableList<LexemeChar> = mutableListOf(),
) {
  override fun toString(): String {
    return "LexemeChar(char='${this.char}')"
  }
}



val stringLexemes = listOf(
  "sin", "tan", "arctan", "tg", "ctg", "arcctg",
)

val root = run {
  val root = LexemeChar(null)
  fun buildLexemesTree(){
    // number
    val digits = listOf(
      LexemeChar('0'),
      LexemeChar('1'), LexemeChar('2'), LexemeChar('3'),
      LexemeChar('4'), LexemeChar('5'), LexemeChar('6'),
      LexemeChar('7'), LexemeChar('8'), LexemeChar('9'),
    )
    digits.forEach { d ->
      d.next += digits
      d.next += root
    }
    root.next += digits
    
    val numberDot = LexemeChar('.')
    numberDot.next += digits
    root.next += numberDot
    
    val digitsWithDot = listOf(
      LexemeChar('0'),
      LexemeChar('1'), LexemeChar('2'), LexemeChar('3'),
      LexemeChar('4'), LexemeChar('5'), LexemeChar('6'),
      LexemeChar('7'), LexemeChar('8'), LexemeChar('9'),
    )
    digits.forEach { d ->
      d.next += digitsWithDot
      d.next += numberDot
      d.next += root
    }
    
    
    
    // basic operators
    val basicOperators = listOf(
      LexemeChar('+'), LexemeChar('-'), LexemeChar('*'), LexemeChar('/')
    )
    basicOperators.forEach { it.next += root }
    root.next += basicOperators
    
    
    // parentheses
    val parentheses = listOf(LexemeChar('('), LexemeChar(')'))
    parentheses.forEach { it.next += root }
    root.next += parentheses
    
    
    // todo
    //  ['a']
    //  ['r']
    //  ['c']
    //  ['c']
    //  ['t', 'o']
    //  ['g', 't']
    //  надо в текущей итерации проверить, есть ли такая буква уже
    //  (но ветви деоева не могут сойтись, если они хоть раз разошлись, иначе будет путаница в дальнейшем с тем куда идти)
    // test
    val test = mutableListOf(root)
    test += stringLexemes[0].map { LexemeChar(it) }
    for (i in 1..test.lastIndex){
      test[i-1].next += test[i]
    }
    test.last().next += root
    
  }
  buildLexemesTree()
  root
}









data class Lexema(
  val type: String,
  val data: String,
  val s: Int,
  val e: Int,
)
/*
data class LexemesParsingException(
  val index: Int
): RuntimeException("$index")
*/



fun String.lexify(): List<Lexema> {
  val lexemes: MutableList<Lexema> = mutableListOf()
  val lexemeChars: MutableList<LexemeChar> = mutableListOf(root)
  var s = 0
  
  var i = 0
  while (i<=this.length){
    val lexemaChar =
      if (i<this.length) lexemeChars.last().next.find { it.char==this[i] }
      else null
    
    if (lexemaChar!=null) lexemeChars += lexemaChar
    
    if (lexemaChar==null) {
      val canEnd = lexemeChars.last().next.contains(root)
      lexemes += Lexema(
        type = if (canEnd) "" else "error",
        data = lexemeChars
          .slice(1..lexemeChars.lastIndex)
          .joinToString(separator = "") { it.char!!.toString() },
        s = s,
        e = i
      )
      lexemeChars.clear()
      lexemeChars += root
      s = i
    }
    
    if (lexemaChar!=null || i==this.length) i++
  }
  
  return lexemes
}
