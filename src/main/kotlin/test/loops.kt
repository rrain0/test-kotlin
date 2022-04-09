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