package test.`run-commands`

import utils.cast
import java.io.File
import java.util.concurrent.TimeUnit





fun main(){
    listOf("ffmpeg").runIt()

    println();println();println();

    listOf("ffmpeg").runCommand()

    println();println();println();

    println(listOf("ffmpeg").runCommandWithResult())

    println();println();println();
}




private fun String.runIt() = ProcessBuilder(this).inheritIO().start().waitFor()
private fun List<String>.runIt() = ProcessBuilder(this).inheritIO().start().waitFor()





// redirect process output into console
private fun String.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long? = null,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
) = this.split(Regex("""\s""")).runCommand(workingDir,timeoutAmount,timeoutUnit)

private fun List<String>.runCommand(
    workingDir: File = File("."),
    timeoutAmount: Long? = null,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
) {
    ProcessBuilder(this)
        .directory(workingDir)
        .inheritIO()
        .start()
        .also { if (timeoutAmount==null) it.waitFor() else it.waitFor(timeoutAmount, timeoutUnit) }
}









// redirect process output to string
private fun String.runCommandWithResult(
    workingDir: File = File("."),
    timeoutAmount: Long? = null,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
) = this.split(Regex("""\s""")).runCommandWithResult(workingDir,timeoutAmount,timeoutUnit)

private fun List<String>.runCommandWithResult(
    workingDir: File = File("."),
    timeoutAmount: Long? = null,
    timeoutUnit: TimeUnit = TimeUnit.SECONDS
): List<String>? = runCatching {
    ProcessBuilder(this)
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .also { if (timeoutAmount==null) it.waitFor() else it.waitFor(timeoutAmount, timeoutUnit) }
        .let { listOf(
            it.inputStream.bufferedReader().readText(), // stdout
            it.errorStream.bufferedReader().readText() //stderr
        ) }
}.onFailure { it.printStackTrace() }.getOrNull()