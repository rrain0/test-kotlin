package utils


inline fun <reified T> Any?.cast(): T = this as T


infix fun String.dot(other: String) = this+"."+other

fun println(vararg args: Any?) = kotlin.io.println(args.joinToString(" "))


/*inline fun <T : Any> Any?.tryor(defaultValue: T, block: ()->T): T {
    return try { block() ?: defaultValue } catch (_: Throwable) { defaultValue }
}*/
