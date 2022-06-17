package geometry

data class Point2(val x:Double, val y:Double) : Comparable<Point2> {

    // Need to range creation
    override fun compareTo(other: Point2) = compareByXY(other)

    // сравнить сначала по X потом по Y
    fun compareByXY(other: Point2): Int {
        return compareByX(other)
            .let { if (it==0) compareByY(other) else it }
    }

    fun compareByX(other: Point2): Int {
        return if (x>other.x) 1
        else if (x<other.x) -1
        else 0
    }
    fun compareByY(other: Point2): Int {
        return if (y>other.y) 1
        else if (y<other.y) -1
        else 0
    }

    operator fun minus(b: Point2) = Point2(x-b.x, y-b.y)
    operator fun plus(b: Point2) = Point2(x+b.x, y+b.y)
    // Need to range creation
    operator fun rangeTo(other: Point2) = Point2IteratorAndRange(this,other)

    companion object {
        val ZERO = Point2(0.0, 0.0)
    }

}


// CUSTOM RANGE IMPLEMENTATION
class Point2IteratorAndRange(
    override val start: Point2, override val endInclusive: Point2, val lenStep: Double = 1.0
)
    : Iterator<Point2>, ClosedRange<Point2>, Iterable<Point2>
{
    init {
        if (lenStep <= 0.0) throw IllegalArgumentException("Step must be positive, now step=$lenStep")
    }

    val len = Vector2(start, endInclusive).len()
    val step = Vector2(start, endInclusive).withLen(lenStep).zeroBased().b

    var current = start
    //var nextVal = start+step


    override fun next(): Point2 {
        current += step
        if (Vector2(start, current).len() > len) current = endInclusive
        return current
    }

    //override fun hasNext() = Vector2(start, nextVal).len() <= len
    override fun hasNext() = current!=endInclusive



    override fun iterator(): Iterator<Point2> = this

    infix fun step(lenStep: Double) = Point2IteratorAndRange(start, endInclusive, lenStep)
}

