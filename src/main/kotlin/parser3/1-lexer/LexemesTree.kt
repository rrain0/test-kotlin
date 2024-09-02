package parser3.`1-lexer`




data class LexemeChar(val c: Char)



data class LexemeNode(
  val c: LexemeChar? = null,
  var isEnd: Boolean = false,
  val next: MutableList<LexemeNode> = mutableListOf()
) {
  val isLast get() = next.isEmpty()
  
  fun add(lexemeChar: LexemeChar, isEnd: Boolean = false): LexemeNode {
    val node: LexemeNode
    val existingNode = this.next.find { it.c == lexemeChar }
    if (existingNode != null) {
      if (existingNode.isEnd && isEnd) throw RuntimeException("this node already an end node")
      if (isEnd) existingNode.isEnd = true
      node = existingNode
    }
    else {
      val newNode = LexemeNode(lexemeChar, isEnd)
      this.next += newNode
      node = newNode
    }
    return node
  }
  
}



fun LexemeNode.addFunctions(functionTokens: List<String>) {
  functionTokens.forEach { t ->
    if (t.isEmpty()) throw RuntimeException("Token length cannot be 0")
    var node = this
    t.forEachIndexed { i, c ->
      node = node.add(LexemeChar(c), i == t.lastIndex)
    }
  }
}

