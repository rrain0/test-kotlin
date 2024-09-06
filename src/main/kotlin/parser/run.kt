package parser

import parser.`1-lexer`.*
import util.println

/*
Пробелы: " ", "\n", "\t", ...
Это обычные токены, но дргугие токены не должны начинаться с первых символов токенов-пробелов.

Операторы: "+", "-", "*", "/", ...
Это обычные токены, но дргугие токены не должны начинаться с первых символов токенов-операторов.

Цифры: "12", "12.3", ...
Особое специально сконструированное рекурсивное поддерево лексем.
Дргугие токены не должны начинаться с первых символов токенов-цифр.

Обычные функции: "sin", "cos", "arcsin", ...

 */


// add numbers without trailing dot
fun LexemeNode.addNumbers() {
  val digits = "0123456789".map { LexemeChar(it) }
  val dot = LexemeChar('.')
  
  val dotNode = LexemeNode(dot)
  
  // digits before dot
  digits.map { this.add(it, isEnd = true, type = LexemeType.NUMBER) }
    .also { d -> d.forEach { it.next += d } }
    .onEach { it.next += dotNode }
  
  // digits after dot
  digits.map { dotNode.add(it, isEnd = true, type = LexemeType.NUMBER) }
    .also { d -> d.forEach { it.next += d } }
}

fun String.eval(lexemesTree: LexemeNode) {
  val input = this
  
  val lexemes = input.lexify(lexemesTree)
  
  println("lexemes for", """"$input":""", lexemes)
  println("tokens for", """"$input":""", lexemes.map { it.token })
}


fun main() {
  // output must be: "sin", "v", "a", "invariant"
  "sinvainvariant".eval(LexemeNode().apply {
    addStaticTokens(listOf("sin", "inv", "invariant"))
  })
  
  // output must be: "sin", "a", " ", "+", " ", "cos", "(", "a", ")"
  "sina + cos(a)".eval(LexemeNode().apply {
    addStaticTokens(listOf("(", ")", "+", "-", "*", "/", "^", "**"))
    addStaticTokens(listOf("sin", "cos", "tan", "arctan", "tg", "ctg", "arcctg"))
  })
  
  // output must be:
  // "sin", "a", " ", "+", " ", "cos", "(", "a", ")",
  // "+", "tg", "1", " ", "+", " ", "ctg", "(", "1.5", ")"
  "sina + cos(a)+tg1 + ctg(1.5)".eval(LexemeNode().apply {
    addStaticTokens(listOf("(", ")", "+", "-", "*", "/", "^", "**"))
    addNumbers()
    addStaticTokens(listOf("sin", "cos", "tan", "arctan", "tg", "ctg", "arcctg"))
  })
  
  // output must be:
  // "1123", "+", "878", "+", "(", "123", "/", "8", "*", "9", "-", "0", ")"
  "1123+878+(123/8*9-0)".eval(LexemeNode().apply {
    addStaticTokens(listOf("(", ")", "+", "-", "*", "/", "^", "**"))
    addNumbers()
    addStaticTokens(listOf("sin", "cos", "tan", "arctan", "tg", "ctg", "arcctg"))
  })
  
  // output must be:
  // "-", "12.34", "+", ".", "878", "+", "(", "123", ".", "/", "8", "*", "9", "-", "0.1", ")"
  "-12.34+.878+(123./8*9-0.1)".eval(LexemeNode().apply {
    addStaticTokens(listOf("(", ")", "+", "-", "*", "/", "^", "**"))
    addNumbers()
    addStaticTokens(listOf("sin", "cos", "tan", "arctan", "tg", "ctg", "arcctg"))
  })
  
  // output must be:
  // "sin", "(", "12.3", "+", "s", "i", "(", "7.6", ".", "5", ".", ")"
  "sin(12.3)+si(7.6.5.)".eval(LexemeNode().apply {
    addStaticTokens(listOf("(", ")", "+", "-", "*", "/", "^", "**"))
    addNumbers()
    addStaticTokens(listOf("sin", "cos", "tan", "arctan", "tg", "ctg", "arcctg"))
  })
}