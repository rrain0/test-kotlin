package ui

/*
TODO KTML (Kotlin Text Markup Language)
1) реализовать теневой рендер элемента с определённым контентом, чтобы можно было взять у него высоту и ширину
   и присвоить другому элементу с таким же родителем.
   
2) Вместо рефов автоматически мы находимся в контексте элемента и берём его свойства.
   Нужно реализовать get & set, которые будут делать подписку на изменения.
   
3) Автосвязка эелементов путём прокидывания элемента, к которому хотим привязаться.
   Это вместо html label.
   Возможность указать какие атрибуты связывать (hover, focus, active, ...).
   
4) w, h, s (size: w & h),
   ar (aspectRatio),
   t, r, b, l,
   cx, cy, (cl, ct),
   tFromB, bFromT, lFromR, rFromL,
   cxFromR, cyFromB, (cr, cb),
   type:
     | number (or number with units)
     | true (means 'same as parent': for w/h is 100%, for t, r, ... is 0)
     | undef (unset value)
   
5) Любое количество псевдоэлементов с кастомными именами.
   Возможность задать, в какой псевдоэлемент пойдёт контент текущего элемента.
   
6) Текущий элемент задал ширину.
   Вложенный элемент принял ширину + имеет свой aspect ratio (например от картинки), определяющий высоту.
   Текущий элемент принял высоту вложенного.
   Или. У родителя задана ширина. Дальше рендерится контент, который когда отрендерился, даёт высоту.
   Эта высота считывается соседним к этому элементом, из неё вычисляется новая высота текущего элемента.
   <div w=300>
     <img getAutoHeightByParentWidthAndImgAspectRatio />
     <fade asParent/>
   </div>
   
7) gap-start & gap-end instead of padding/margin left/right.
   Gap will be applied only if it is between elements (gap will not be applied if element is first/last in row/col).
   Allow negative gap.
   
8) Эвенты.
   ev.defaultActions.<action>.prevent().
   Разделить эвенты по уровням:
     1) Raw event: Device code, device action code.
     2) Simple event: mouse button down, mouse button up, LCtrl down, RCtrl down
     3) Complex event: pointer down, pointer up, Ctrl down
     4) Ui event: click, hover / unhover
   Дать возможность передавать массив листенеров.
   
9) Рамка для начала отсчёта координат элемента.
   divA1 {
     divA2 {
       divA3 {
         // Родителем для координат позиционирования элемента будет не divA2, а divA1
         frame = divA1.frame
       }
     }
   }
   divB1 {
     divB2 {
         // Родителем для координат позиционирования элемента будет не divB1, а divA2
       frame = divA2.frame
     }
   }
   
10) children -> nodes, next, nextNodes

11) Event binding. Заменить вот такую кострукцию на прокидывание эвентов span в button.
    <label>
      <button>icon</button>
      <span>text</span>
    </label>

12) element height from current line height

13) Приоритеты для margin, padding, content, content min / max, width, height...
    Возможно даже сделать глобальные приоритеты.
    Приоритет расширения / сужения элементов.
    
    Например есть колонка из 3 элементов, сначала расширить центральный элемент по ширине,
    высота у него через aspect-ratio,
    потом расширить по высоте нижний и верхний элементы, не влияя на высоту центрального.
    Но если верхнему и нижнему не хватает места, то они уменьшат центральный (высокий приоритет min-height).
    
13.1) Распределение элементов layout через priority & fraction.
    Fraction делит пропорционально в рамках текущего приоритета.
    
14) p?.[OnlineGameMode.Blitz]?.games as Partial<*>

15) Возможность выбирать box в который будет позиционироваться данный элемент.
    Дать возможность элементам выбирать,
    будут ли позиционироваться относительно паддинга или относительно границ элемента.
    Полезно, когда одному элементу в колонке требуется занять всю ширину, игноря паддинги.
    placeInContainer: element | padding
    
16) Медиа-запросы от высоты / ширины элемента, условное уменьшение текста, если размер слишком большой
    Создание псевдо элементов от буквы - сами символы как выбираемые элементы.
    
17) Неявная переменная для li, хранящая порядковый номер и можно задать функцию рендера номера

18) Возможность задать векторную форму вьюхе и сдлеть у этой формы параметры
    (типа border-radius для закруглённого прямоугольника)
    
19) Предефайнить слоты у контейнера для вьюх

20) Возможность рендерить несколько layout flow в одном контейнере
  <div>
    <flex>...</flex> // сами эти элементы не создают фактических элементов, а только определяют новый layout
    <flex>...</flex>
    <grid>...</grid>
    <stack>...</stack>
    <absolute>...</absolute>
  </div>
  
21) Дать возможность создавать свой LAYOUT !!!

22) Разделённый узел.
Здесь <div id='row1'/> & <div id='row2'/> разделены на 2 части.
Это нужно чтобы напрмер взять максимальную высоту контента из 2 колонок.
Либо надо делать какие-то привязки по высоте между 2 дивами - общая высота.
<div>
  <div>
    <div id='row1'><div> col 1 row 1 <div/></div>
    <div id='row2'><div> col 1 row 2 <div/></div>
  </div>
  <div>
    <div id='row1'><div> col 2 row 1 <div/></div>
    <div id='row2'><div> col 2 row 2 <div/></div>
  </div>
</div>

23) Использовать псевдоэлемент для настройки внутреннего элемента

*/



enum class NodeType(val typeString: String) {
  DIV("div"), // division
  TEXT("text"), // text
  A("a"), // anchor
  ;
}

open class Node(val type: NodeType = NodeType.DIV) {
  var up: Node? = null
  val down: MutableList<Node> = mutableListOf()
  
  inner class ValueHandler {
    val dependencies: MutableList<ValueHandler> = mutableListOf()
    val consumers: MutableList<ValueHandler> = mutableListOf() // subscribers
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



