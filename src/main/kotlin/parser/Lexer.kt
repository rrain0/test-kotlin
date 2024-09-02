package parser




enum class LexemeType {
  ERROR,
  NUMBER,
  //KEYWORD,
  OPERATOR,
  FUNCTION,
  PARENTHESES,
}



data class LexemeChar(
  val type: LexemeType?,
  val char: Char?,
) {
  var next: MutableList<LexemeChar> = mutableListOf()
}



val functionStringLexemes = listOf(
  "sin", "tan", "arctan", "tg", "ctg", "arcctg",
)

val root = run {
  val root = LexemeChar(null, null)
  fun buildLexemesTree(){
    // number
    val digits = listOf(
      LexemeChar(LexemeType.NUMBER, '0'),
      LexemeChar(LexemeType.NUMBER, '1'),
      LexemeChar(LexemeType.NUMBER, '2'),
      LexemeChar(LexemeType.NUMBER, '3'),
      LexemeChar(LexemeType.NUMBER, '4'),
      LexemeChar(LexemeType.NUMBER, '5'),
      LexemeChar(LexemeType.NUMBER, '6'),
      LexemeChar(LexemeType.NUMBER, '7'),
      LexemeChar(LexemeType.NUMBER, '8'),
      LexemeChar(LexemeType.NUMBER, '9'),
    )
    digits.forEach { d ->
      d.next += digits
      d.next += root
    }
    root.next += digits
    
    val numberDot = LexemeChar(LexemeType.NUMBER, '.')
    numberDot.next += digits
    root.next += numberDot
    
    val digitsWithDot = listOf(
      LexemeChar(LexemeType.NUMBER, '0'),
      LexemeChar(LexemeType.NUMBER, '1'),
      LexemeChar(LexemeType.NUMBER, '2'),
      LexemeChar(LexemeType.NUMBER, '3'),
      LexemeChar(LexemeType.NUMBER, '4'),
      LexemeChar(LexemeType.NUMBER, '5'),
      LexemeChar(LexemeType.NUMBER, '6'),
      LexemeChar(LexemeType.NUMBER, '7'),
      LexemeChar(LexemeType.NUMBER, '8'),
      LexemeChar(LexemeType.NUMBER, '9'),
    )
    digits.forEach { d ->
      d.next += digitsWithDot
      d.next += numberDot
      d.next += root
    }
    
    
    
    // basic operators
    val basicOperators = listOf(
      LexemeChar(LexemeType.OPERATOR, '+'),
      LexemeChar(LexemeType.OPERATOR, '-'),
      LexemeChar(LexemeType.OPERATOR, '*'),
      LexemeChar(LexemeType.OPERATOR, '/'),
    )
    basicOperators.forEach { it.next += root }
    root.next += basicOperators
    
    
    // parentheses
    val parentheses = listOf(
      LexemeChar(LexemeType.PARENTHESES, '('),
      LexemeChar(LexemeType.PARENTHESES, ')'),
    )
    parentheses.forEach { it.next += root }
    root.next += parentheses
    
    
    // functions
    functionStringLexemes.forEach { s ->
      if (s.isEmpty()) throw RuntimeException("Token length cannot be 0")
      var curr = root
      s.forEach { c ->
        val next = curr.next.find { it.char==c }
          ?: LexemeChar(LexemeType.FUNCTION, c).also { curr.next += it }
        curr = next
      }
      curr.next += root
    }
    
    
  }
  buildLexemesTree()
  root
}








data class Lexeme(
  val type: LexemeType,
  val data: String,
  val s: Int,
  val e: Int,
  val nextExpectedChars: List<LexemeChar> = listOf()
)



fun String.lexify(): List<Lexeme> {
  val lexemes: MutableList<Lexeme> = mutableListOf()
  val lexemeChars: MutableList<LexemeChar> = mutableListOf(root)
  var s = 0
  
  var i = 0
  while (i <= this.length){
    val lexemaChar =
      if (i < this.length) lexemeChars.last().next.find { it.char==this[i] }
      else null
    
    if (lexemaChar!=null) lexemeChars += lexemaChar
    
    if (lexemaChar==null) {
      val last = lexemeChars.last()
      val canEnd = last.next.contains(root)
      lexemes += Lexeme(
        type = if (canEnd) last.type!! else LexemeType.ERROR,
        data = lexemeChars
          .slice(1..lexemeChars.lastIndex)
          .joinToString(separator = "") { it.char!!.toString() },
        s = s,
        e = i,
        nextExpectedChars = last.next
      )
      lexemeChars.clear()
      lexemeChars += root
      s = i
    }
    
    if (lexemaChar!=null || i==this.length) i++
  }
  
  return lexemes
}
