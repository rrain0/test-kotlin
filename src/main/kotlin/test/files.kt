package test

import java.io.File
import java.io.FileWriter






/*
    We can invoke the use function on any object which implements AutoCloseable or Closeable,
    just as with try-with-resources in Java.

    The method takes a lambda expression, executes it, and disposes of the resource of (by calling close() on it)
    whenever execution leaves the block, either normally or with an exception.

    So, in this case, after use, the writer is no longer usable, because Kotlin has automatically closed it.
 */
fun useTest(){

  val writer = FileWriter("test.txt")
  writer.use { writer.write("something") }

  FileWriter("test.txt").use { w -> w.write("something") }

  FileWriter("test.txt").use { it.write("something") }
}



fun filesTest(){

  val logicalDrives = File.listRoots().toList()
  println("logicalDrives: $logicalDrives") // [C:\, D:\, E:\, F:\, G:\, K:\, L:\, M:\]

  println()
  println()

  // Получить логический диск, на котором исполняется программа
  println(File("/")) // \
  println(File("/").absoluteFile) // D:\
  println(File("/").listFiles()?.toList())

  println()
  println()

  var physicalDrive0 = File("""\\.\PhysicalDrive0""")
  physicalDrive0 = physicalDrive0.absoluteFile
  println("""\\.\PhysicalDrive0 exists: ${physicalDrive0.exists()}""")

  """C:""".let { println("$it -> ${File(it).absoluteFile}") } // C:\
  """/C:""".let { println("$it -> ${File(it).absoluteFile}") } // C:\
  """C:\\""".let { println("$it -> ${File(it).absoluteFile}") } // C:\
  """C://""".let { println("$it -> ${File(it).absoluteFile}") } // C:\

  // резолвит такой путь от корня текущего диска, на котором исполняется программа
  """/C""".let { println("$it -> ${File(it).absoluteFile}") }  // D:\C
  """/some-path""".let { println("$it -> ${File(it).absoluteFile}") }  // D:\some-path
}




fun main() = filesTest()