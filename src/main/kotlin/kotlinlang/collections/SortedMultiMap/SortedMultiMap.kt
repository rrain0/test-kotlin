package kotlinlang.collections.SortedMultiMap

import com.google.common.collect.TreeMultimap
import com.rrain.utils.base.number.ifZero
import java.util.UUID
import kotlin.compareTo



data class P(val id: UUID, val x: Double, val y: Double) {
  companion object {
    val identityComparator = Comparator<P> { a, b -> a.id compareTo b.id }
    val coordinatesComparator = Comparator<P> { a, b ->
      a.x compareTo b.x ifZero { a.y compareTo b.y }
    }
  }
}

fun main() {
  val sortedMap = TreeMultimap.create(P.coordinatesComparator, P.identityComparator)
  P(UUID.fromString("205bd192-5955-4ba0-8f63-dd649a983f29"), 0.0, 0.0)
    .also { sortedMap.put(it, it) }
  P(UUID.fromString("0d632539-83d8-430b-8a01-261600a048ae"), 0.0, 0.0)
    .also { sortedMap.put(it, it) }
  P(UUID.fromString("d3aa9c70-bd99-4ee1-a038-fd7030f0db1e"), 1.0, 1.0)
    .also { sortedMap.put(it, it) }
  P(UUID.fromString("547ad3d8-d743-4fec-ad31-c7694374043e"), -1.0, 1.0)
    .also { sortedMap.put(it, it) }
  val p = P(UUID.fromString("bd58493c-8d2a-404b-b747-d962b994becf"), 1.0, -1.0)
    .also { sortedMap.put(it, it) }
  P(UUID.fromString("78ebc240-22db-4f70-84c6-e9fa24183292"), -1.0, -1.0)
    .also { sortedMap.put(it, it) }
  
  p.also { sortedMap.remove(it, it) }
  
  // =>
  // P(id=78ebc240-22db-4f70-84c6-e9fa24183292, x=-1.0, y=-1.0)
  // P(id=547ad3d8-d743-4fec-ad31-c7694374043e, x=-1.0, y=1.0)
  // P(id=0d632539-83d8-430b-8a01-261600a048ae, x=0.0, y=0.0)
  // P(id=205bd192-5955-4ba0-8f63-dd649a983f29, x=0.0, y=0.0)
  // P(id=d3aa9c70-bd99-4ee1-a038-fd7030f0db1e, x=1.0, y=1.0)
  sortedMap.values().forEach { println(it) }
}