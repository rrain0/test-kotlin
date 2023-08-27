import kotlin.system.measureTimeMillis
import kotlin.time.TimeSource


fun main(args: Array<String>) {
  // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
  

  println("replace: ${uppercaseWords("name fAm otch")}")

  run {
    println("Hello Kotlin! ${listOf(1,2,3,0).sortedBy { it }}")


    val list: List<String> = listOf("dsf")
    val mutableList: MutableList<String> = mutableListOf("dsf")


    //timeMeasurement()


    listOf(1).forEach(fun(it){ println(it) })
    listOf(1).forEach { println(it) }



    val sum = fun(a:Int, b:Int): Int { return a+b }
    fun runIntAction(a: Int, b: Int, action: (Int,Int)->Int): Int {
      return action(a,b)
    }
    runIntAction(4, 10, sum)
    runIntAction(3,6, { a,b -> a*b })
    runIntAction(6,9) { a,b -> a-b }
  }
}



fun test_function() = 0
val test_function_lambda = { 0 }



private fun timeMeasurement(){
  val time = measureTimeMillis {
    repeat(10000) { println("AAA") }
  }
  println("measured time: $time")
}
private fun timeMeasurementMarks(){
  val timeSource = TimeSource.Monotonic
  val mark1 = timeSource.markNow()
  repeat(10000) { println("AAA") }
  val mark2 = timeSource.markNow()
  println("measured time by marks: ${mark2 - mark1}") // measured time by marks: 168.822155ms
}

private fun uppercaseWords(str: String) = str
  .lowercase()
  .replace(Regex("""(?<=(^|\s)).""")) { it.value[0].uppercase() }

