package util.Print



fun println(vararg args: Any?) = kotlin.io.println(args.joinToString(" "))