package test.`map-to-object`

/*
    The object is backed by map
    Changes in object reflects on map and vise versa
 */
fun mapToObjectByKotlin(){
    val pMap = mutableMapOf<String,Any?>("name" to "Person", "age" to 20)
    val p = Person2(pMap)


    println(p)
    pMap["age"] = 18
    println(p)
    p.age = 19
    println(pMap)

}

private class PersonVal(map: Map<String,Any?>){
    val name: String by map
    val age: Int by map
}
private class Person(map: MutableMap<String,Any?>){
    var name: String by map
    var age: Int by map
}


private class Person2(map: MutableMap<String,Any?>){
    var name: String by map
    var age: Int by map

    constructor(name: String, age: Int) : this(mutableMapOf("name" to name, "age" to age))

    override fun toString(): String {
        return "Person2(name='$name', age=$age)"
    }


}

