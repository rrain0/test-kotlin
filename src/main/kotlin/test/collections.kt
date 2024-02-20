
package test

import geometry.Point2
import geometry.Rectangle
import geometry.Shape
import java.util.LinkedList


fun main(){
    collections()
}



/*
    Basic Collections: List, Set, Map
    They can be Read-only or Mutable

    To make like Java Stream API operations, need to convert collection to Sequence
    or else (in case of simple Iterator) after each operation will be created new collection

    Equality of collections do through their basic interfaces (List, Set, Map) regardless of implementation
 */


fun collections(){
    
    run {
        // LIST:
        // ArrayList is the default implementation
        
        // List elements (including nulls) can duplicate: a list can contain any number of equal objects or occurrences
        // of a single object.
        
        
        val readonlyNumbers = listOf("one","two","three","four")
        val mutableNumbers = mutableListOf("one","two","three","four")
        
        mutableNumbers.add("five")
        mutableNumbers.add("five again")
        mutableNumbers[5] = "six" // it's only possible to operate with existing indices
        
        println("mutable list of numbers: $mutableNumbers") // => mutable list of numbers: [one, two, three, four, five, six]
        println("Number of elements: ${mutableNumbers.size}")
        println("Last index: ${mutableNumbers.lastIndex}")
        println("Third element: ${mutableNumbers.get(2)}")
        println("Fourth element: ${mutableNumbers[3]}")
        println("Index of element \"two\": ${mutableNumbers.indexOf("two")}")
        
        mutableNumbers.removeAt(4)
        mutableNumbers.removeAt(4)
        /*
            EQUALITY:
            Two lists are considered equal (==) if they have the same sizes and structurally equal elements at the same positions.
         */
        // Lists are equal:
        println("equality of lists: ${readonlyNumbers==mutableNumbers}") // => true
        var linkedList = LinkedList(readonlyNumbers)
        println("equality of ArrayList and LinkedList: ${readonlyNumbers==linkedList}") // => true
    }
    
    
    run {
        // SET
        // LinkedHashSet is the default implementation - preserves the order of elements insertion (use first() & last() or iterator to get them)
        
        // Two sets are equal if they have the same size,
        // and for each element of a set there is an equal element in the other set.
        
        val readonlyNumbersSet = setOf(4, 7, 10, 1)
        val mutableNumbersSet = mutableSetOf(1, 4, 6)
        
        println("readonlyNumbersSet: $readonlyNumbersSet") // => readonlyNumbersSet: [4, 7, 10, 1]
        println("Number of elements: ${mutableNumbersSet.size}")
        if (mutableNumbersSet.contains(1)) println("1 is in the set")
        println("readonlyNumbersSet.last() == mutableNumbersSet.first(): ${readonlyNumbersSet.last() == mutableNumbersSet.first()}") // => true
    }
    
    
    
    run {
        // MAP
        
        //The default implementation of Map – LinkedHashMap – preserves the order of elements insertion when iterating the map.
        //Two maps containing the equal pairs are equal regardless of the pair order.
        
        val readonlyMap = mapOf("one" to 1, "true" to true, "null" to null)
        val mutableMap = mutableMapOf("one" to 1, "true" to true, "null" to null)
        
        println("readonlyMap: $readonlyMap")
        println("readonlyMap class: ${readonlyMap::class.qualifiedName}")
        println("mutableMap: $mutableMap")
        println("mutableMap class: ${mutableMap::class.qualifiedName}")
        
        println("All keys: ${readonlyMap.keys}")
        println("All values: ${readonlyMap.values}")
        if ("one" in readonlyMap) println("""Value by key "key2": ${readonlyMap["one"]}""")
        if (1 in readonlyMap.values) println("The value 1 is in the map")
        if (readonlyMap.containsValue(1)) println("The value 1 is in the map") // same as 'in' operator
    }
    
    
    
    
    run {
        /*
            The read-only collection types are covariant.
            This means that, if a Rectangle class inherits from Shape,
            you can use a List<Rectangle> anywhere the List<Shape> is required (List is readonly, MutableList is mutable).
            In other words, the collection types have the same subtyping relationship as the element types.
            Maps are covariant on the value type, but not on the key type.

            In turn, mutable collections aren't covariant;
         */
        
        fun printMutableShapes(shapes: MutableCollection<Shape>){
            for (s in shapes) print("$s ")
            println()
        }
        fun printReadonlyShapes(shapes: Collection<Shape>){
            for (s in shapes) print("$s ")
            println()
        }
        
        val mutableRectangles = mutableListOf(Rectangle(Point2(0.0,0.0), Point2(1.0,3.0), Point2(3.0,-1.0), "rect"))
        val readonlyRectangles = listOf(Rectangle(Point2(0.0,0.0), Point2(1.0,3.0), Point2(3.0,-1.0), "rect"))
        
        val mutableShapes = mutableListOf<Shape>(Rectangle(Point2(0.0,0.0), Point2(1.0,3.0), Point2(3.0,-1.0), "rect"))
        val readonlyShapes = listOf<Shape>(Rectangle(Point2(0.0,0.0), Point2(1.0,3.0), Point2(3.0,-1.0), "rect"))
        
        //printMutableShapes(mutableRectangles) // not allowed - mutables are not covariant
        printReadonlyShapes(mutableRectangles) // readonly collections are covariant (Rectangle => Shape)
        //printMutableShapes(readonlyRectangles) // not allowed because need Mutable
        printReadonlyShapes(readonlyRectangles)
        
        printMutableShapes(mutableShapes)
        printReadonlyShapes(mutableShapes)
        //printMutableShapes(readonlyShapes) // not allowed because need Mutable
        printReadonlyShapes(readonlyShapes)
    }
    
    
    
    run {
        // Creating collections
        
        // There is listOf<T>(), setOf<T>(), mutableListOf<T>(), mutableSetOf<T>(), mapOf() and mutableMapOf()
        
        val numbersSet = setOf("one", "two", "three", "four")
        val emptySet = mutableSetOf<String>()
        val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)
        
        /*
            Note that the to notation creates a short-living Pair object,
            so it's recommended that you use it only if performance isn't critical.
            To avoid excessive memory usage, use alternative ways.
            For example, you can create a mutable map and populate it using the write operations.
            The apply() function can help to keep the initialization fluent here.
         */
        
        val numbersMap2 = mutableMapOf<String, String>().apply { this["one"] = "1"; this["two"] = "2" }
        
        
        // Collection builder:
        val map = buildMap { // this is MutableMap<String, Int>, types of key and value are inferred from the `put()` calls below
            this.put("a", 1)
            put("b", 0)
            put("c", 4)
        }
        println(map) // {a=1, b=0, c=4}
        
        
        // Empty collections: emptyList(), emptySet(), and emptyMap():
        val empty = emptyList<String>()
        
        
        // Initializer functions:
        var doubled = List(3, { it * 2 }) // or MutableList if you want to change its content later
        // { it * 2 } - lambda function that accepts index (it cannot changed in this short form)
        var doubled2 = List(3) { it * 2 }
        var doubled3 = List(3, { index -> index * 2 }) // change the default name "it" (it: неявное имя единственного параметра)
        println("doubled list: $doubled") // => doubled list: [0, 2, 4]
        
        
        // Concrete collection:
        // To create a concrete type collection, such as an ArrayList or LinkedList,
        // you can use the available constructors for these types.
        // Similar constructors are available for implementations of Set and Map.
        
        val linkedList = LinkedList<String>(listOf("one", "two", "three"))
        val presizedSet = HashSet<Int>(32)
        
        
        
        // Collection copy:
        val sourceList1 = mutableListOf("Alice", "Bob")
        val copyList = sourceList1.toList()
        
        // Copy to other collections:
        val sourceList2 = mutableListOf(1, 2, 3)
        val copySet = sourceList2.toMutableSet()
        
        
        
        // Put Mutable collection into Read-only variable:
        val sourceList3 = mutableListOf(1, 2, 3)
        val referenceList: List<Int> = sourceList3
        //referenceList.add(4) // compilation error
        sourceList3.add(4)
        println(referenceList) // shows the current state of sourceList
        
        
        
        // Invoke functions on other collections:
        val numbers1 = listOf("one", "two", "three", "four")
        val longerThan3 = numbers1.filter { it.length > 3 } // produces NEW collection in which length of string elem > 3
        println(longerThan3) // => [three, four]
        
        // Mapping produces a list of a transformation results:
        val numbers2 = setOf(1, 2, 3)
        println(numbers2.map { it * 3 }) // => [3, 6, 9]
        println(numbers2.mapIndexed { idx, value -> value * idx }) // => [3, 6, 9]
        
        // Association produces maps:
        val numbers3 = listOf("one", "two", "three", "four")
        println(numbers3.associateWith { it.length }) // => {one=3, two=3, three=5, four=4}
    }
    
    
    run {
        // ITERATORS:
        
        val numbers1 = listOf("one", "two", "three", "four")
        val numbersIterator = numbers1.iterator()
        while (numbersIterator.hasNext()) {
            println(numbersIterator.next())
        }
        
        // for in (contains iterator implicitly):
        val numbers2 = listOf("one", "two", "three", "four")
        for (item in numbers2) {
            println(item)
        }
        
        // forEach:
        val numbers3 = listOf("one", "two", "three", "four")
        numbers3.forEach {
            println(it)
        }
        
        
        
        // List iterator can iterate from end to start:
        val numbers4 = listOf("one", "two", "three", "four")
        val listIterator = numbers4.listIterator()
        while (listIterator.hasNext()) listIterator.next()
        println("Iterating backwards:")
        while (listIterator.hasPrevious()) {
            print("Index: ${listIterator.previousIndex()}")
            println(", value: ${listIterator.previous()}")
        }
        
        
        // Iterators of Mutable Collections are Mutable (remove, add, replace):
        val numbers5 = mutableListOf("one", "two", "three", "four")
        val mutableIterator = numbers5.iterator()
        mutableIterator.next()
        mutableIterator.remove()
        println("After removal: $numbers4")
        
        // Mutable list iterator:
        val numbers6 = mutableListOf("one", "four", "four")
        val mutableListIterator = numbers6.listIterator()
        mutableListIterator.next()
        mutableListIterator.add("two")
        mutableListIterator.next()
        mutableListIterator.set("three")
        println(numbers6)
    }
    
    run {
        // OPERATIONS:
        
        // "in" means "contains"
        val list = listOf("apple", "avocado", "banana", "kiwifruit")
        val set = setOf("apple", "avocado", "banana", "kiwifruit")
        
        val appleInList: Boolean = "apple" in list
        
        // Operations like filter, map, ... create a NEW collection
        // If you want to specify destination collection (mutable), you can use filterTo instead of filter and so on. New content will be appended to destination.
        
        // Write operations:
        // sort() mutate this collection, but sorted() returns NEW collection
        
        list
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.uppercase() }
            .forEach { println(it) }
        
        
        // NEXT: https://kotlinlang.org/docs/collection-transformations.html
    }
    
}