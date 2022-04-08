package test

import java.io.FileWriter

/*
    We can invoke the use function on any object which implements AutoCloseable or Closeable,
    just as with try-with-resources in Java.

    The method takes a lambda expression, executes it, and disposes of the resource of (by calling close() on it)
    whenever execution leaves the block, either normally or with an exception.

    So, in this case, after use, the writer is no longer usable, because Kotlin has automatically closed it.
 */

fun use(){

    val writer = FileWriter("test.txt")
    writer.use { writer.write("something") }

    FileWriter("test.txt").use { w -> w.write("something") }

    FileWriter("test.txt").use { it.write("something") }
}