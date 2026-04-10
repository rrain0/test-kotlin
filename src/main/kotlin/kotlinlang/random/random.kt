package kotlinlang.random

import java.util.Base64
import kotlin.random.Random



fun main() {
  gen128BitKey()
}

fun randomTest() {
  val randomInts = List(10) { Random.nextInt(0 /* inclusive */, 100 /* exclusive */) }
  println(randomInts)

  val randomDoubles = List(10){ Random.nextDouble(-1000.0 /* inclusive */, 1000.0 /* exclusive */) }
  println(randomDoubles)
}

fun gen128BitKey() {
  // 128 бит делим на байт и генерим рандомные байты
  val random128Bits = Random.nextBytes(ByteArray(128 / 8))
  val random128BitsAsBase64 = Base64.getEncoder().encodeToString(random128Bits)
  println("random128BitsAsBase64: $random128BitsAsBase64")
}