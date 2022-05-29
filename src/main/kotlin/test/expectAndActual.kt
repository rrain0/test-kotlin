package test.`expect-and-actual`



/*
    If you're developing a multiplatform application that needs to access platform-specific APIs
    that implement the required functionality (for example, generating a UUID),
    use the Kotlin mechanism of expected and actual declarations.

    With this mechanism, a common source set defines an expected declaration,
    and platform source sets must provide the actual declaration that corresponds to the expected declaration.
    This works for most Kotlin declarations,
    such as functions, classes, interfaces, enumerations, properties, and annotations.
 */


// Examples: https://kotlinlang.org/docs/multiplatform-connect-to-apis.html#examples



/*


// Common
expect interface Mascot {
    open fun display(): String
}

// Platform-specific
actual interface Mascot {
    actual fun display(): String {
        TODO()
    }
}

class MascotImpl : Mascot {
    // it's ok not to implement `display()`: all `actual`s are guaranteed to have a default implementation
}


*/



/*

expect class AtomicRef<V>(value: V) {
    fun get(): V
    fun set(value: V)
    fun getAndSet(value: V): V
    fun compareAndSet(expect: V, update: V): Boolean
}

// Actual already implemented in library
actual typealias AtomicRef<V> = java.util.concurrent.atomic.AtomicReference<V>


 */