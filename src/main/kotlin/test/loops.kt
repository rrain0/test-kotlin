package test

fun loops(){

    val items = listOf("apple", "banana", "kiwifruit")

    // "in" is "contains"
    // but in for loop it means iterate over Iterable
    val b = "apple" in items

    // while

    var i = 0
    while (i < items.size){
        println("item at index $i is ${items[i]}")
        i++
    }

    // do while
    do {

    } while (i++<200)




    // for loop

    // iterates over Iterable instances
    for (item in items){
        println("item is $item")
    }

    // iterate in range
    for (index in items.indices){
        println("item at index $index is ${items[index]}")
    }
    for (ii in 4..8) println(ii)



    // iterate with index and value
    for ((index,value) in listOf(1,10,100).withIndex())
        println("the element at $index is $value")


    // destructuring
    for((key,value) in mapOf("1" to 1));
    /*
        To do so, you must implement operators:
            operator fun <K, V> Map<K, V>.iterator(): Iterator<Map.Entry<K, V>> = entrySet().iterator()
            operator fun <K, V> Map.Entry<K, V>.component1() = getKey()
            operator fun <K, V> Map.Entry<K, V>.component2() = getValue()
     */


    // repeat loop
    repeat(5) { println("index: $it") }



    // without body
    while(i++<100);

    // tagged loop
    whileloop@while(i++<300){
        if (i>250) break@whileloop
        if (i>240) continue@whileloop
        if (i>230) continue
        if (i>250) break
    }
}