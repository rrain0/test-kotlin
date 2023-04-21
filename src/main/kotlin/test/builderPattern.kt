package test.`builder-pattern`

import java.nio.file.Path
import kotlin.io.path.Path




// more convenient builder pattern
data class Input private constructor(val path: Path){


  class BuilderConfig {
    var path: String = ""
  }

  companion object {

    operator fun invoke(build: BuilderConfig.()->Unit) = Input.BuilderConfig()
      .apply(build)
      .let{ this.build(it) }

    // some convenient raw data processing and then build
    fun build(builderConfig: BuilderConfig): Input {
      return Input(Path.of(builderConfig.path).toAbsolutePath())
    }

  }

}







// standard builder pattern
private fun SourceBuilderUsage(){
  val source1 = Source.build {
    paths.add("G:/test.txt")
    paths.add("G:/test2.txt")
    tag = "#test"
  }
}
private data class Source private constructor(val paths:List<Path>, val readThreadId:Any, val tag:Any?){
  constructor(paths:List<Path>, readThreadId:Any?, tag:Any?, second:Boolean = true)
  : this(paths.toList(), readThreadId ?: Object(), tag)

  //
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



