package parser2.`1-lexer`


data class Lexeme(
  val type: LexemeType,
  val token: String,
  val s: Int,
  val e: Int,
)


fun String.lexify(lexemesTree: LexemeNode): List<Lexeme> {
  val lexemes: MutableList<Lexeme> = mutableListOf()
  var lexemeNodes: MutableList<LexemeNode> = mutableListOf(lexemesTree)
  var lastEndNodeI = 0
  var s = 0
  
  var i = 0; while (i <= length) {
    val node =
      if (i < length) lexemeNodes.last().next.find { it.c == this[i] }
      else null
    
    if (node != null) {
      lexemeNodes += node
      if (node.isEnd) lastEndNodeI = lexemeNodes.lastIndex
      i++
    }
    else {
      val canEnd = lastEndNodeI > 0
      
      if (canEnd) {
        lexemes.add(Lexeme(
          lexemeNodes[lastEndNodeI].type!!,
          lexemeNodes
            .slice(0..lastEndNodeI)
            .joinToString("") { it.c!!+"" },
          i - lastEndNodeI,
          i,
        ))
        i = lastEndNodeI + 1
        lexemeNodes = mutableListOf(lexemesTree)
        lastEndNodeI = 0
      }
      
      /*val last = lexemeNodes.last()
      val canEnd = last.next.contains(root)
      lexemes += parser.Lexeme(
        type = if (canEnd) last.type!! else parser.LexemeType.ERROR,
        data = lexemeNodes
          .slice(1..lexemeNodes.lastIndex)
          .joinToString(separator = "") { it.char!!.toString() },
        s = s,
        e = i,
        nextExpectedChars = last.next
      )
      lexemeNodes.clear()
      lexemeNodes += root
      s = i*/
    }
    
  }
  
  return lexemes
}
