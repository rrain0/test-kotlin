package parser2.`1-lexer`




enum class LexemeMode {
  // first mode requires char tree
  FIRST, // use first match: + - * / ( )
  NUMBER, // use special number pattern
  SPACE, // use special space pattern
  // FIRST can interrupt DYNAMIC
  DYNAMIC, // use dynamic last match: function names, keywords
}

enum class LexemeType {
  UNKNOWN,
  NUMBER,
  OPERATOR,
  FUNCTION, // keyword is special type of function
  PARENTHESES,
}

data class LexemeChar(
  val c: Char?,
  val type: LexemeType? = null,
)

fun LexemeChar.toNode() = LexemeNode(this.c, this.type)

data class LexemeNode(
  val c: Char? = null, // null if root or end of string
  var type: LexemeType? = null, // null if it is not end node
  val next: MutableList<LexemeNode> = mutableListOf(),
) {
  
  val isEnd get() = this.type != null
  
  fun add(lexemeChar: LexemeChar): LexemeNode {
    val node: LexemeNode
    val existingNode = this.next.find { it.c == lexemeChar.c }
    if (existingNode != null) {
      if (lexemeChar.type != null && existingNode.type != null) throw RuntimeException("this node already an end node")
      if (lexemeChar.type != null) this.type = lexemeChar.type
      node = existingNode
    }
    else {
      val newNode = LexemeNode(lexemeChar.c, lexemeChar.type)
      this.next += newNode
      node = newNode
    }
    return node
  }
  
}


private fun LexemeNode.addNumbers() {
  val digits = listOf(
    LexemeChar('0', LexemeType.NUMBER),
    LexemeChar('1', LexemeType.NUMBER),
    LexemeChar('2', LexemeType.NUMBER),
    LexemeChar('3', LexemeType.NUMBER),
    LexemeChar('4', LexemeType.NUMBER),
    LexemeChar('5', LexemeType.NUMBER),
    LexemeChar('6', LexemeType.NUMBER),
    LexemeChar('7', LexemeType.NUMBER),
    LexemeChar('8', LexemeType.NUMBER),
    LexemeChar('9', LexemeType.NUMBER),
  )
  val dot = LexemeChar('.')
  
  val digitDotNodes = digits.map { this.add(it) }
  
  val dotNode = dot.toNode()
  digitDotNodes.forEach { it.next += dotNode }
  
  digitDotNodes.forEach { it.next += digitDotNodes }
  
  val digitNodes = digits.map { dotNode.add(it) }
  digitNodes.forEach { it.next += digitNodes }
}

private fun LexemeNode.addBasicOperators() {
  val basicOperators = listOf(
    LexemeChar('+', LexemeType.OPERATOR),
    LexemeChar('-', LexemeType.OPERATOR),
    LexemeChar('*', LexemeType.OPERATOR),
    LexemeChar('/', LexemeType.OPERATOR),
  )
  basicOperators.forEach { this.add(it) }
}

private fun LexemeNode.addParentheses() {
  val parentheses = listOf(
    LexemeChar('(', LexemeType.PARENTHESES),
    LexemeChar(')', LexemeType.PARENTHESES),
  )
  parentheses.forEach { this.add(it) }
}


private fun LexemeNode.addFunctions(functionTokens: List<String>) {
  functionTokens.forEach { t ->
    if (t.isEmpty()) throw RuntimeException("Token length cannot be 0")
    var node = this
    t.forEachIndexed { i, c ->
      node = node.add(LexemeChar(
        c,
        if (i == t.lastIndex) LexemeType.FUNCTION else null
      ))
    }
  }
}

fun buildLexemesTree(): LexemeNode {
  val lexemesTree = LexemeNode()
  lexemesTree.apply {
    addNumbers()
    addBasicOperators()
    addParentheses()
    val functionTokens = listOf("sin", "tan", "arctan", "tg", "ctg", "arcctg")
    addFunctions(functionTokens)
  }
  return lexemesTree
}

