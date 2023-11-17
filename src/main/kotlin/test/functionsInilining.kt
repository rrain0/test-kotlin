package test.functionsInlining





/*
    Content of inline functions is inlined to call sites.
    "inline" makes that, so when you call function from parameter,
    compiler doesn't create a function object and copies code from function into body of this function.
    So you can't store inlined functions in variables because they are not objects yet.

    Inlining cause generated code grow.
    Inlining speed up program because no need Function object.
    You can reify generic type parameters with fun <reified T> (){}
 */
private inline fun inlineFun(inlined: () -> Unit, noinline notInlined: () -> Unit) {
    inlined()
    notInlined()
    //val inlF = inlined // error - function already not an object and it is simply code that copied into this function instead of call statement
    val notinlF = notInlined
}


/*
    Crossinline.
    Note that some inline functions may call the lambdas passed to them as parameters not directly from the function body,
    but from another execution context, such as a local object or a nested function.
    In such cases, non-local control flow is also not allowed in the lambdas.
    To indicate that the lambda parameter of the inline function cannot use non-local returns,
    mark the lambda parameter with the crossinline modifier:
 */
private inline fun fInline(crossinline body: () -> Unit) {
    val f = object: Runnable {
        override fun run() = body()
    }
    // ...
}

val pi = Math.PI

private inline fun inlineFun(arg: String, func: (String) -> Unit) {
    func(arg)
}
private inline fun crossInlineFun(arg: String, crossinline func: (String) -> Unit) {
    func(arg)
}
private fun crossInlineTest(arg: Array<String>) {
    inlineFun("Hello") {
        println(it)
        return // will return from crossInlineTest
    }
    crossInlineFun("Hello") {
        println(it)
        //return // not allowed to use local returns - compile error
    }
    println("Main finished")
}