package parser3

import parser3.`1-lexer`.LexemeNode
import parser3.`1-lexer`.addFunctions
import parser3.`1-lexer`.lexify
import util.println


fun buildLexemesTree(): LexemeNode {
  val lexemesTree = LexemeNode()
  lexemesTree.apply {
    //addNumbers()
    //addBasicOperators()
    //addParentheses()
    val functionTokens = listOf("sin", "inv", "invariant")
    //val functionTokens = listOf("sin", "tan", "arctan", "tg", "ctg", "arcctg")
    addFunctions(functionTokens)
  }
  return lexemesTree
}


fun main() {
  
  val lexemesTree = buildLexemesTree()
  
  val input = "sinvainvariant"
  // output must be "sin", "v", "a", "invariant"
  
  println("lexemes for", input, input.lexify(lexemesTree))
}