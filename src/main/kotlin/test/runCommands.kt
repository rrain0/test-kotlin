package test.`run-commands`

import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit





fun main(){
    listOf("utilities").runIt()
    
    println();println();println();
    
    listOf("utilities").runCommand()
    
    println();println();println();
    
    println(listOf("utilities").runCommandWithResult())
    
    println();println();println();
}




private fun String.runIt() = ProcessBuilder(this).inheritIO().start().waitFor()
private fun List<String>.runIt() = ProcessBuilder(this).inheritIO().start().waitFor()





// redirect process output into console
private fun String.runCommand(
    workingDir: File = File("."),
    timeout: Duration? = null,
) = this.split(Regex("""\s""")).runCommand(workingDir,timeout)

private fun List<String>.runCommand(
    workingDir: File = File("."),
    timeout: Duration? = null,
) {
    ProcessBuilder(this)
        .directory(workingDir)
        .inheritIO()
        .start()
      .also {
        if (timeout==null) it.waitFor()
        else it.waitFor(timeout.toLong(DurationUnit.MILLISECONDS), TimeUnit.MILLISECONDS)
      }
}









// redirect process output to string
fun String.runCommandWithResult(
    workingDir: File = File("."),
    timeout: Duration? = null,
): List<String>? = this.split(Regex("""\s""")).runCommandWithResult(workingDir,timeout)

fun List<String>.runCommandWithResult(
    workingDir: File = File("."),
    timeout: Duration? = null,
): List<String>? = runCatching {
    ProcessBuilder(this)
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .also {
            if (timeout==null) it.waitFor()
            else it.waitFor(timeout.toLong(DurationUnit.MILLISECONDS), TimeUnit.MILLISECONDS)
        }
        .let { listOf(
            it.inputStream.bufferedReader().readText(), // stdout
            it.errorStream.bufferedReader().readText(), // stderr
        ) }
}.onFailure { it.printStackTrace() }.getOrNull()