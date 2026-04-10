package com.rrain.utils.base.file

import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.file.Paths



object FileUtils {
  
  // path relative to "resources" folder
  fun getResourceAsStream(relPath: String): InputStream = (
    object { }::class.java.getResourceAsStream("/$relPath")
      ?: throw FileNotFoundException(relPath)
  )
  
  // Get project folder absolute path or path to executing jar...
  // e.g. "D:\PROG\Kotlin\[projects]\test-kotlin"
  val absPath = Paths.get("").toAbsolutePath()
  
}