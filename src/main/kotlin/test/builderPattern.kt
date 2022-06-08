package test.`builder-pattern`

import java.nio.file.Path
import kotlin.io.path.Path


data class Source private constructor(val paths:List<Path>, val readThreadId:Any, val tag:Any?){
    constructor(paths:List<Path>, readThreadId:Any?, tag:Any?, second:Boolean = true)
            : this(paths.toList(), readThreadId ?: Object(), tag)

    private constructor(builder: Builder) : this(builder.paths.map(::Path), builder.readThreadId, builder.tag)
    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder(
        val paths: MutableList<String> = mutableListOf()
    ){
        var readThreadId:Any? = null
        var tag:Any? = null

        fun build() = Source(this)
    }
}