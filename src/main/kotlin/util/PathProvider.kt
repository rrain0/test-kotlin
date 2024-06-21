package util

import java.nio.file.Paths




class PathProvider {
  companion object {
    // get path to project folder, e.g. "D:\PROG\Kotlin\[projects]\kotlin-test"
    val absPath = Paths.get("").toAbsolutePath()
  }
}