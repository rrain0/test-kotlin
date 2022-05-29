package test.`scope-functions`


fun scopeFunctions(){
    // let run with also apply
    /*
          Function    Object reference    Return value      Is extension function
        ● let         it                  Lambda result     Yes
        ● run         this                Lambda result     Yes
          run         -                   Lambda result     No: called without the context object
        ● with        this                Lambda result     No: takes the context object as an argument.
        ● apply       this                Context object    Yes
        ● also        it                  Context object    Yes
     */
    /*
        Use cases:
        ● Executing a lambda on non-null objects: let
        ● Introducing an expression as a variable in local scope: let
        ● Object configuration: apply
        ● Object configuration and computing the result: run
        ● Running statements where an expression is required: non-extension run
        ● Additional effects: also
        ● Grouping function calls on an object: with
     */


    class Turtle {
        fun penDown(){/*...*/}
        fun penUp(){/*...*/}
        fun turn(degrees: Double){/*...*/}
        fun forward(pixels: Double){/*...*/}
    }

    val myTurtle = Turtle()

    // Call multiple methods on an object instance (with)
    with(myTurtle) { //draw a 100 pix square
        penDown()
        for (i in 1..4) {
            forward(100.0)
            turn(90.0)
        }
        penUp()
    }

    // Configure properties of an object (apply)
    myTurtle.apply { //draw a 100 pix square
        penDown()
        for (i in 1..4) {
            forward(100.0)
            turn(90.0)
        }
        penUp()
    }

    // Swap two variables
    var a = 1
    var b = 2
    a = b.also { b = a }
}