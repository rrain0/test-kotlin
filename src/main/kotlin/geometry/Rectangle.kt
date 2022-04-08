package geometry

class Rectangle(var a:Point2, var b:Point2, var c: Point2, name:String): Shape(name) {

    override fun area(): Double = (a.x-b.x)*(c.y-b.y) - (c.x-b.x)*(a.y-b.y)
}