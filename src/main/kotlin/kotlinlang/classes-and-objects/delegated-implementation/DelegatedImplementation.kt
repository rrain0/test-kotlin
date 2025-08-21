package kotlinlang.`classes-and-objects`.`delegated-implementation`



interface Base {
  fun print()
}

class BaseImpl(val x: Int) : Base {
  override fun print() { print(x) }
}

class Derived(b: Base) : Base by b

fun delegatedInterfaceImplementation() {
  val base = BaseImpl(10)
  Derived(base).print()
}
