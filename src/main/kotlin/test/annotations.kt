
// AnnotationTarget.FILE
// Such annotation must be at the top level of file
@file:SomeAnnotation

package test.annotations

import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties


fun main(){
    annotations()
}



fun annotations(){

    println(
        "AAB.aa has @EntityId: ${
            AAB::class.memberProperties
                .find { it.name=="aa" }!!
                .hasAnnotation<EntityId>()
        }"
    )
    println(
        "AAB.ab has @EntityId: ${
            AAB::class.memberProperties
                .find { it.name=="ab" }!!
                .hasAnnotation<EntityId>()
        }"
    )

}



@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class EntityId



private class AAB (
    @EntityId
    var aa: String
){
    @EntityId
    var ab: String? = null
}






// Where the annotation will be (source, binary, runtime)
@Retention(AnnotationRetention.SOURCE)
// What can be annotated
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.FILE,
    AnnotationTarget.PROPERTY,
)
// Can you use annotation multiple times at same place
// For repeatable annotation will be generated class-container @SomeAnnotation.Container, or you can use custom class via @JvmRepeatable(CustomClassContainer::class)
// To extract repeatable annotations via reflection use KAnnotatedElement.findAnnotations()
@Repeatable
// Specifies that the annotation is part of the public API and should be included in the class or method signature shown in the generated API documentation.
@MustBeDocumented
annotation class SomeAnnotation(
    val include: Array<String> = [],
    vararg val exclude: String = ["some param"],
    val number: Double = 10.0,
    // Parameter types:
    // ● only vals
    // ● only nonnulls
    // ● can be array / vararg
    // ● can have default value
    // ● allowed types: primitives (Int, Long, ...), String, Enum, classes, other annotations
)

// AnnotationTarget.FUNCTION or EXPRESSION
val bazzz: Fun = @SomeAnnotation { println("I am lambda") }

// Repeatable
// AnnotationTarget.TYPEALIAS
@SomeAnnotation(["a"],"b", "c", number = 11.0)
@SomeAnnotation(arrayOf("a","b"), *arrayOf("c"))
typealias Fun = ()->Unit

@SomeAnnotation // AnnotationTarget.CLASS
class Foo {

    @SomeAnnotation // AnnotationTarget.FUNCTION
    fun baz(@SomeAnnotation foo: Int):  // AnnotationTarget.VALUE_PARAMETER
            @SomeAnnotation Int { // AnnotationTarget.TYPE
        return (@SomeAnnotation 1) // AnnotationTarget.EXPRESSION
    }

    fun <@SomeAnnotation T> bazz(a: T) = a // AnnotationTarget.TYPE_PARAMETER

    val prop @SomeAnnotation get() = 9 // AnnotationTarget.PROPERTY_GETTER (not PROPERTY)

    @SomeAnnotation // AnnotationTarget.CONSTRUCTOR
    constructor(a: Int)

    // Annotate receiver value parameter
    // AnnotationTarget.VALUE_PARAMETER
    // without @receiver the receiver type Int will be annotated
    fun @receiver:SomeAnnotation Int.aaa() = this

}

// Target shortcut for annotations
// Multiple annotations shortcut
class Example(
    @field:[SomeAnnotation SomeAnnotation(["a"])] val foo: Any,     // annotate Java field (AnnotationTarget.FIELD), Multiple annotations shortcut
    @get:SomeAnnotation val bar: Any,       // annotate Java getter
    @param:SomeAnnotation val quux: Any,    // annotate Java constructor parameter
)





