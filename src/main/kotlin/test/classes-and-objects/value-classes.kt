package test.`classes-and-objects`.`value-classes`





fun main(){
  valueClassTest()
}



/*
  Классы-значения.
  Обладают всеми свойствами классов-данных,
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

@JvmInline
value class Person(private val fullName: String) {
  // Allowed since Kotlin 1.4.30:
  init {
    check(fullName.isNotBlank()) {
      "Full name shouldn't be empty"
    }
  }
  // Allowed by default since Kotlin 1.9.0:
  constructor(name: String, lastName: String) : this("$name $lastName") {
    check(lastName.isNotBlank()) {
      "Last name shouldn't be empty"
    }
  }
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