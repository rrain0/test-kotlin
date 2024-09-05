package parser.`1-lexer`


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
    return (1..lastEndI).joinToString(separator = "") { chain[it].c!!.c.toString() }
  }
}


fun String.lexify(root: LexemeNode): List<Lexeme> {
  //println("string:", this, "len:", length)
  
  val lexemes: MutableList<Lexeme> = mutableListOf()
  var s = 0
  val chains: MutableList<LexemeChain?> = (0..length)
    .map { LexemeChain(0, false, mutableListOf(root)) }
    .toMutableList()
  
  for (i in 0..length) {
    val c = if (i < length) this[i] else null
    var chainI = s; while(chainI <= i) {
      val chain = chains[chainI]!!
      if (!chain.isBroken) {
        val nextNode = chain.chain.last().nextFor(c)
        // найдено продолжение текущей цепочки
        if (nextNode != null) {
          chain.chain.add(nextNode)
          if (nextNode.isEnd) chain.lastEndI = chain.chain.lastIndex
        }
        // не найдено начало пустой цепочки - делаем ошибочную цепочку из 1 символа
        else if (chain.isEmpty) {
          val singleWrongNode = LexemeNode(c?.let { LexemeChar(it) }, true)
          chain.chain.add(singleWrongNode)
          chain.lastEndI = 1
          chain.isBroken = true
        }
        // не найдено продолжение цепочки
        else {
          chain.isBroken = true
        }
      }
      if (s < length && chainI == s && chain.isBroken) {
        val token = when {
          !chain.isEmpty -> chain.getToken()
          c == null -> ""
          else -> c.toString()
        }
        val e = s + token.length
        //println("c:", c, "token:", token, "s:", s, "e:", e)
        lexemes += Lexeme(token, s, e)
        // clear memory from old chains
        (s..<e).forEach { chains[it] = null }
        s = e
        chainI = e
      }
      else chainI++
    }
  }
  
  return lexemes
}

