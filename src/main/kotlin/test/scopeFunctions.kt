package test


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
}