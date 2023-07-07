package test.`extension-properties`





fun main(){

}


private val Int.seconds get() = this*1000
private val Int.minutes get() = this*60*1000

fun extensionPropertiesTest(){
  val oneSecond = 1.seconds
  val twentySeconds = 20.seconds
  val thirtyMinutes = 30.minutes

  println("1.second = $oneSecond milliseconds")
  println("20.seconds = $twentySeconds milliseconds")
  println("30.minutes = $thirtyMinutes milliseconds")
}


