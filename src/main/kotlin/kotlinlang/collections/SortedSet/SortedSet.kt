package kotlinlang.collections.SortedSet

import com.rrain.util.base.number.ifZero
import java.util.TreeSet


class P(val x: Double, val y: Double) {
  override fun toString(): String {
    return "P(x=$x, y=$y)"
  }
}

// TreeSet or TreeMap only uses compareTo of Comparable or provided comparator.
// They don't use equals or hashCode.
fun main() {
  val sortedSet = TreeSet<P> { a, b -> a.x.compareTo(b.x).ifZero { a.y.compareTo(b.y) } }
  
  sortedSet += P(0.0, 0.0)
  sortedSet += P(1.0, 1.0)
  sortedSet += P(-1.0, 1.0)
  sortedSet += P(1.0, -1.0)
  sortedSet += P(-1.0, -1.0)
  
  sortedSet -= P(1.0, -1.0)
  
  // => [P(x=-1.0, y=-1.0), P(x=-1.0, y=1.0), P(x=0.0, y=0.0), P(x=1.0, y=1.0)]
  println(sortedSet)
}