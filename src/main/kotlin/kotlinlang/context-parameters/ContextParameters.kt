package kotlinlang.`context-parameters`



fun main() {
  testNestedContexts()
  testSameNestedContexts()
}


object UserService {
  fun log(message: String) = println(message)
  fun findUserById(id: Int): String = "User $id"
}

context(id: Int)
fun UserService.findById() = findUserById(id)

fun testContext(): String? {
  val id = 10
  return id.run {
    UserService.findById()
  }
}




context(int: Int, bool: Boolean)
fun String.intBoolStr() = "$int $bool $this"

fun testNestedContexts() {
  val first = (
    5.run {
      true.run {
        "str".intBoolStr()
      }
    }
  )
  println(first) // 5 true str
  
  val second =(
    true.run {
      5.run {
        "str".intBoolStr()
      }
    }
  )
  println(second) // 5 true str
}




context(name: String, lastName: String)
fun String.nameLastName() = "$this$name$lastName"

fun testSameNestedContexts() {
  val str = (
    "name".run {
      "lastName".run {
        "@".nameLastName()
      }
    }
  )
  // Пока что котлин не различает вложенные контексты одинакового типа
  println(str) // @lastNamelastName
}




// Declares a function with a context parameter
context(users: UserService)
fun outputMessage(message: String) {
  // Uses log from the context
  users.log("Log: $message")
}

// Declares a property with a context parameter
context(users: UserService)
val firstUser: String
  // Uses findUserById from the context
  get() = users.findUserById(1)


// Uses "_" as context parameter name - makes it inaccessible by the name inside block
context(_: UserService)
fun logWelcome() {
  // Finds the appropriate log function from UserService
  outputMessage("Welcome!")
}

