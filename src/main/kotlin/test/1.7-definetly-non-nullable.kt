package test

/*
    Now limited to nullable upper bounds on the left side of & and non-nullable Any on the right side:
    <Nullable Type> & Any:
 */

private fun <T> elvisLike(x: T, y: T & Any): T & Any = x ?: y
private fun <T> T.orElse(other: T & Any): T & Any = this ?: other

fun definitelyNonNullable(){

    // OK
    elvisLike<String>("","").length
    "".orElse("").length

    // Error: 'null' cannot be a value of a non-null type
    //elvisLike<String>("", null).length
    //"".orElse(null).length

    // OK
    elvisLike<String?>(null,"").length
    null.orElse("").length

    // Error: 'null' cannot be a value of a non-null type
    //elvisLike<String?>(null,null).length
    //null.orElse(null).length
}