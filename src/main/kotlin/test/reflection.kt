package test

import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.reflect


/*

    dependencies {
        implementation(kotlin("reflect"))
    }

 */

fun reflectionTest(){
    val test3 = Test3()

    println("Test3 is abstract: ${::Test3.isAbstract}")
    println("test3.prop is final: ${test3::prop.isFinal}")
    println("test3.f is infix: ${test3::f.isInfix}")

    println("Test3 memberProperties: ${Test3::class.memberProperties}")
}

private class Test3{

    val prop = ""

    fun f(){}

}