package geometry

import kotlin.math.sqrt


data class Vector2 (val a: Point2, val b: Point2) {
    fun lenSq() = (b.x-a.x).let { it*it } + (b.y-a.y).let { it*it }
    fun len() = sqrt(lenSq())

    fun withLen(newLen: Double): Vector2 {
        val scale = newLen/len()
        return Vector2(a, Point2(a.x + (b.x-a.x)*scale, a.y + (b.y-a.y)*scale))
    }

    fun zeroBased() = Vector2(Point2.ZERO, b-a)
}