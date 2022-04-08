
package test

fun sequences() {
    // SEQUENCES
    /*
        This is analog of Java Stream API

        When the processing of an Iterable includes multiple steps, they are executed eagerly:
        each processing step completes and returns its result – an intermediate collection.
        The following step executes on this collection.
        In turn, multi-step processing of sequences is executed lazily when possible:
        actual computing happens only when the result of the whole processing chain is requested.
     */

    val numbersSequence1 = sequenceOf("four", "three", "two", "one")

    val numbers1 = listOf("one", "two", "three", "four")
    val numbersSequence2 = numbers1.asSequence()


    // Generate:
    // generation stops if function returns null, otherwise it's infinite

    val oddNumbers1 = generateSequence(1) { it + 2 } // `it` is the previous element
    println(oddNumbers1.take(5).toList())
    //println(oddNumbers.count())     // error: the sequence is infinite

    val oddNumbersLessThan10 = generateSequence(1) { if (it < 8) it + 2 else null }
    println(oddNumbersLessThan10.count())


    /*
        Generate by function that accepts lambda generator:

        Finally, there is a function that lets you produce sequence elements one by one or by chunks of arbitrary sizes –
        the sequence() function.
        This function takes a lambda expression containing calls of yield() and yieldAll() functions.
        They return an element to the sequence consumer and suspend the execution of sequence() until
                the next element is requested by the consumer. yield() takes a single element as an argument;
        yieldAll() can take an Iterable object, an Iterator, or another Sequence.
        A Sequence argument of yieldAll() can be infinite. However, such a call must be the last:
        all subsequent calls will never be executed.
     */


    val oddNumbers2 = sequence {
        yield(1)
        yieldAll(listOf(3, 5))
        yieldAll(generateSequence(7) { it + 2 })
    }
    println(oddNumbers2.take(5).toList())



    /*
        The sequence operations can be classified into the following groups regarding their state requirements:

        Stateless operations require no state and process each element independently,
        for example, map() or filter(). Stateless operations can also require
        a small constant amount of state to process an element, for example, take() or drop().

        Stateful operations require a significant amount of state, usually proportional
        to the number of elements in a sequence.

        If a sequence operation returns another sequence, which is produced lazily,
        it's called intermediate. Otherwise, the operation is terminal. Examples of terminal operations are toList()
        or sum(). Sequence elements can be retrieved only with terminal operations.

        Sequences can be iterated multiple times;
        however some sequence implementations might constrain themselves to be iterated only once.
        That is mentioned specifically in their documentation.



        Methods of iterable/sequence take(<Int>) or drop(<Int>)
     */


    val words = "The quick brown fox jumps over the lazy dog".split(" ")
    // convert the List to a Sequence:
    val wordsSequence = words.asSequence()

    val lengthsSequence = wordsSequence.filter { println("filter: $it"); it.length > 3 }
        .map { println("length: ${it.length}"); it.length }
        .take(4)

    println("Lengths of first 4 words longer than 3 chars")
    // terminal operation: obtaining the result as a List
    println(lengthsSequence.toList())
}