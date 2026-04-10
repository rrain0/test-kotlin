package kotlinlang.`files-and-resources`.`java-nio`

import java.nio.file.Files
import java.nio.file.Paths





fun main() {

}



fun getFileSize(path: String) = Files.size(Paths.get(path))
