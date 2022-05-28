package test.reflection

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability


/*

    dependencies {
        implementation(kotlin("reflect"))
    }

 */

fun main(){
    reflection()
}

fun reflection(){
    val test3 = Test3()

    println("Test3 is abstract: ${::Test3.isAbstract}")
    println("test3.prop is final: ${test3::prop.isFinal}")
    println("test3.f is infix: ${test3::f.isInfix}")

    println("Test3 memberProperties: ${Test3::class.memberProperties}")





    // ПОИСК КОНСТРУКТОРА И СОДАНИЕ ОБЪЕКТА ИЗ МАПЫ
    val entityInMap = mapOf(
        "id" to 5,
        "name" to "some entity",
        "tag" to "#entity"
    )
    var parametersMap = mutableMapOf<KParameter,Any?>()
    val ctor = Entity::class.constructors.find {
        parametersMap.clear()
        it.parameters.all {
            if (it.name in entityInMap && it.type.classifier == entityInMap[it.name]!!::class){
                parametersMap.put(it,entityInMap[it.name])
                return@all true
            } else if (it.isOptional) return@all true
            else return@all false
        }
        return@find parametersMap.size==entityInMap.size
    }
    ctor!!
    Entity::class.constructors.forEach{ c ->
        println(c.name)
        println(c.parameters)
    }
    println("NEW ENTITY: ${ctor.callBy(parametersMap)}")




    run {
        class Test(val v: Direction? = Direction.UP)
        println("local Test constroctors cnt: ${Test::class.constructors.size}")
        println("return type: ${Test::class.memberProperties.first().returnType}")
        println("isSubtype: ${Test::class.memberProperties.first().returnType.withNullability(true).isSubtypeOf(Direction::class.starProjectedType.withNullability(true))}")
    }

    run {
        println("Direction is Enum: ${Direction::class.isOnlySubtype(Enum::class)}") // => true
        println("Direction is Enum: ${Direction::class.isSubtype(Enum::class)}") // => true
    }

    run {
        val short: Short = 45
        val int: Int = 65
        println(int::class.starProjectedType)
        println(Int::class.starProjectedType)
        println(int::class.starProjectedType == Int::class.starProjectedType)
        println(Entity::class.memberProperties.find { it.name=="id" }!!.returnType.withNullability(false) == Int::class.starProjectedType)
        println("Direction enum type name: ${Direction::class.starProjectedType.toString()}") // => Direction enum type name: test.reflection.Direction
        println("${AA::class.starProjectedType.toString()}") // => test.reflection.AA<*>
    }
    run{
        val i: Int? = 8
        println("i!!::class.qualifiedName: ${i!!::class.qualifiedName}")
    }
}


private infix fun KClass<*>.isOnlySubtype(c: KClass<*>): Boolean {
    return this.starProjectedType.withNullability(false).isSubtypeOf(c.starProjectedType.withNullability(false))
}
private infix fun KClass<*>.isSubtype(c: KClass<*>): Boolean {
    return this.starProjectedType.isSubtypeOf(c.starProjectedType)
}

enum class Direction { UP, DOWN, LEFT, RIGHT }

private class AA<A>{}
private class Test3{

    val prop = ""

    fun f(){}

}


class Entity(
    val id: Int,
    val name: String,
    var tag: String?
){

    constructor(id: Int, name: String) : this(id, name, "tag")
    constructor(id: Int) : this(id, "name", "tag")
    override fun toString(): String {
        return "Entity(id=$id, name='$name', tag=$tag)"
    }
}