package parser3.`1-lexer`


/*
⯈ Запретить начинаться с некоторых символов ("+", "-", "*", "/", "(", ")", ...) кастомным функциям

⯈ есть "sin", "cos" и ввод "sincos":
Находим "sin", "cos" - принахождении "c" откатываемся к последнему успешному токену.

⯈ есть "expsin", "exp", "sin" и ввод "expsin":
Находим "expsin", игнорим "exp" - берём самый длинный возможный токен.

⯈ есть "sin", "inv" и ввод "sinaasinvsin":
Находим "sin", "a", "a", "sin", "v", "sin" - берём первые совпажающие токены.

⯈ есть "+", "++" и ввод "++":
Находим "++" - берём наиболее длинный совпадающий токен.

 */



/*
Нахождение токена:
⯈ есть "sin", "inv" и ввод "sinaasinv":
Находим "sin", "a", "a", "sin", "v" - берём первые совпажающие токены.
[0+0]: "s"
[0+1]: "si", "i"
[0+2]: "sin", "in"
[3+0]: "sin" => Lexem, "in" was dropped, "a"
[4+0]: "a" => Lexem, "a"
[5+0]: "a" => Lexem, "s"
[5+1]: "si", "i"
[5+2]: "sin", "in",
[5+3]: "sin" => Lexem, "in" was dropped, "v"

● Если нашёлся токен, который начинается раньше всех, то берём его сразу.
есть "sin", "inv" и ввод "sinv" => "sin", "v"

● Если нашёлись 2 токена с одним началом, то берём самый длинный.
есть "inv", "invariant" и ввод "invariant" => "invariant"
 */



data class Lexeme(
  val token: String,
  val s: Int,
  val e: Int,
)

fun LexemeNode.nextFor(c: Char?): LexemeNode? {
  return next.find { it.c?.c == c }
}

data class LexemeChain(
  var lastEndI: Int,
  var isBroken: Boolean,
  val chain: MutableList<LexemeNode>, // first node always root
) {
  val isEmpty get() = lastEndI == 0
  fun getToken(): String {
    return (0..lastEndI).joinToString(separator = "") { chain[it].c.toString() }
  }
}

fun String.lexify(root: LexemeNode): List<Lexeme> {
  val lexemes: MutableList<Lexeme> = mutableListOf()
  var s = 0
  val chains: MutableList<LexemeChain?> = (0..length)
    .map { LexemeChain(0, false, mutableListOf(root)) }
    .toMutableList()
  
  var i = 0; while (i <= length) {
    val c = if (i < length) this[i] else null
    (s..i).forEach { ii ->
      val chain = chains[ii]!!
      if (!chain.isBroken) {
        val nextNode = chain.chain.last().nextFor(c)
        if (nextNode != null) {
          chain.chain.add(nextNode)
          if (nextNode.isEnd) chain.lastEndI = chain.chain.lastIndex
        }
        else {
          chain.isBroken = true
        }
      }
      if (ii == s) {
        if (chain.isBroken) {
          if (i < length) {
            val token = if (chain.isEmpty) c.toString() else chain.getToken()
            val e = s + token.length
            lexemes += Lexeme(token, s, e)
            // clear memory
            (s until e).forEach { chains[it] = null }
            s = e
          }
        }
      }
    }
    i++
  }
  
  return lexemes
}

