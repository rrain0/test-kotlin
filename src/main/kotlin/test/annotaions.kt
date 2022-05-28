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