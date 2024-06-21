package ui

/*
TODO
1) реализовать теневой рендер элемента с определённым контентом, чтобы можно было взять у него высоту и ширину
   и присвоить другому элементу с таким же родителем.
*/



enum class NodeType(val typeString: String) {
  DIV("div"),
  TEXT("text"),
  A("a"),
  ;
}

open class Node(val type: NodeType = NodeType.DIV) {
  var up: Node? = null
  val down: MutableList<Node> = mutableListOf()
  
  inner class ValueHandler {
    val dependencies: MutableList<ValueHandler> = mutableListOf()
    val consumers: MutableList<ValueHandler> = mutableListOf()
    var from: ValueHandler? = null
    var value: Value? = null
  }
  
  operator fun String.unaryPlus(){
    val n = this@Node
    val t = this
    n.text(t)
  }
  
  private val widthHandler = ValueHandler()
  fun setWidth(value: Value, from: ValueHandler? = null){
    widthHandler.value = value
    widthHandler.from = from
  }
  fun getWidth() = widthHandler
  
  var width: Value? = null
  var height: Value? = null
}
typealias NodeAction = (Node.()->kotlin.Unit)?

class Text(val text: String) : Node(NodeType.TEXT)
class A(val href: String) : Node(NodeType.A)


fun Node?.createAttachedNode(type: NodeType, block: NodeAction = null): Node {
  val node = Node(type)
  node.up = this
  if (this!=null) down += node
  if (block!=null) node.block()
  return node
}

fun div(block: NodeAction = null): Node = null.createAttachedNode(NodeType.DIV, block)
fun Node?.div(block: NodeAction = null) { this.createAttachedNode(NodeType.DIV, block) }

fun text(text: String, block: NodeAction = null): Node = null.createAttachedNode(NodeType.TEXT, block)
fun Node?.text(text: String, block: NodeAction = null) { this.createAttachedNode(NodeType.TEXT, block) }

fun a(block: NodeAction = null): Node = null.createAttachedNode(NodeType.A, block)
fun Node?.a(block: NodeAction = null) { this.createAttachedNode(NodeType.A, block) }



enum class Unit {
  PERCENT,
  PX,
}
abstract class Value
class NumberValue(val value: Double, val unit: Unit) : Value()
class KeywordValue(val value: String) : Value()

val Int.px get()=NumberValue(this.toDouble(), Unit.PX)
val Double.px get()=NumberValue(this, Unit.PX)
val Int.percent get()=NumberValue(this.toDouble(), Unit.PERCENT)
val Double.percent get()=NumberValue(this, Unit.PERCENT)

val default = KeywordValue("default")
val full = KeywordValue("full")



