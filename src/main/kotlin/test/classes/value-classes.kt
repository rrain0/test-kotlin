package test.classes.`value-classes`





fun main(){
  valueClassTest()
}



/*
  Классы-значения.
  Обладают всемси свойствами классов-данных,
  но по скорости они такие же быстрые, как будто класса обёртки нету.
  На данный момент поддерживается только 1 значение.
  Не имеют дополнительных накладных расходов в виде самого класса обёртки над значениями.
  Компилятор считает такое просто псевдонимом типа.
 */
@JvmInline
private value class Weight(val value: Double)
@JvmInline
private value class Temperature(val value: Double){
  init {
    if (value < 0.0) throw IllegalArgumentException("Temperature in Kelvin cannot be less than zero")
  }
}
private fun proceedTemperature(temperature: Temperature){
  println(temperature)
}

fun valueClassTest(){
  var weight = Weight(65.0)
  //weight = 90.0 // compile error
  var weightDouble = 47.0
  weightDouble = weight.value

  try {
    var temperature = Temperature(-1.0)
  } catch (e: IllegalArgumentException){
    println(e)
  }

  var temperature = Temperature(0.0)
  proceedTemperature(temperature) // Temperature(value=0.0)


}