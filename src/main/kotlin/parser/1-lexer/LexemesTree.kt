package parser.`1-lexer`




data class LexemeChar(val c: Char)

enum class LexemeType {
  UNKNOWN,
  FUNCTION,
  NUMBER,
}

/*
if isEnd is false then type must be UNKNOWN (but no vice versa)
 */
data class LexemeNode(
  val c: LexemeChar? = null,
  var isEnd: Boolean = false,
  var type: LexemeType = LexemeType.UNKNOWN,
  val next: MutableList<LexemeNode> = mutableListOf(),
) {
  val isLast get() = next.isEmpty()
  
  fun add(
    lexemeChar: LexemeChar,
    isEnd: Boolean = false,
    type: LexemeType = LexemeType.UNKNOWN,
  ): LexemeNode {
    val node: LexemeNode
    val existingNode = this.next.find { it.c == lexemeChar }
    if (existingNode != null) {
      if (existingNode.isEnd && isEnd) throw RuntimeException("this node already an end node")
      if (isEnd) {
        existingNode.type = type
        existingNode.isEnd = true
      }
      node = existingNode
    }
    else {
      val newNode = LexemeNode(lexemeChar, isEnd, type)
      this.next += newNode
      node = newNode
    }
    return node
  }
  
}



fun LexemeNode.addStaticTokens(functionTokens: List<String>) {
  functionTokens.forEach { t ->
    if (t.isEmpty()) throw RuntimeException("Token length cannot be 0")
    var node = this
    t.forEachIndexed { i, c ->
      node = node.add(
        LexemeChar(c),
        isEnd = i == t.lastIndex,
        type = if (i == t.lastIndex) LexemeType.FUNCTION else LexemeType.UNKNOWN,
      )
    }
  }
}

