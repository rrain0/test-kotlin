package ffmpeg

import java.nio.file.Path



fun main(){
  ffmpegTest()
}


fun ffmpegTest(){

  run {
    // Prologue
    // Gekijouban Fairy Tail Prologue Hajimari no Asa

    val video1080pAndJapSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[210] Fairy Tail Movie 1 Sp.mkv""" }
    val anythingGroupSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[210] Fairy Tail Movie 1 Sp.mka""" }
    val aniLibriaSound = Input { path="""K:\temp Fairy Tail Movie 1\Prologue sound AniLibria\Fairy Tail Movie 1 Prologue AniLibria.mp4""" }
    val sub = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + sub + AniLibria\sub\Fratelli & Timecraft & BW\Gekijouban Fairy Tail Prologue Hajimari no Asa.ass""" }

    val movieTitle = "Fairy Tail Movie 1 Prologue - Hajimari no Asa (Утро Зарождения)"
    val output = Output { path="""K:\temp Fairy Tail Movie 1\$movieTitle.mkv""" }

    val ffmpeg = ffmpeg {
      fileMetadata.apply {
        title = movieTitle
      }

      // video 1080p
      stream(video1080pAndJapSound).v().n(1).apply {
        codec.apply{
          type = CodecType.H265
          preset = Preset(PresetType.SLOW)
          crf = 20
        }
        metadata.apply {
          title = movieTitle
          language = Language.JAPANESE
          isDefault = true
        }
      }

      // audio Anything Group
      stream(aniLibriaSound).a().n(1).apply {
        codec.apply {
          type = CodecType.OGG
          bitrate = "128k"
        }
        metadata.apply {
          title = "AniLibria"
          language = Language.RUSSIAN
          isDefault = true
        }
      }

      // audio Anything Group
      stream(anythingGroupSound).a().n(1).apply {
        codec.apply {
          type = CodecType.OGG
          bitrate = "192k"
        }
        metadata.apply {
          title = "Anything Group (Dajana & Sad_Kit)"
          language = Language.RUSSIAN
        }
      }
      // audio jap
      stream(video1080pAndJapSound).a().n(1).apply {
        codec.apply {
          type = CodecType.OGG
          bitrate = "192k"
        }
        metadata.apply {
          title = "Original"
          language = Language.JAPANESE
        }
      }

      // subtitles rus
      stream(sub).s().n(1).apply {
        codec.apply {
          type = CodecType.ASS // todo
        }
        metadata.apply {
          title = "Fratelli & Timecraft & BW"
          language = Language.RUSSIAN
          isDefault = true
        }
      }

      output(output)
    }

    println(ffmpeg.buildCommand())
  }

  run {
    // Movie
    // Gekijouban Fairy Tail Houou no Miko

    val video1080pAndJapSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[211] Fairy Tail Movie 1.mkv""" }
    val anidubSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie + AniDUB\[AniDub]_Fairy_Tail_Houou_no_Miko_[Rus]_[DVDRip720p_h264_Ac3]_[MVO].mp4""" }
    val aniLibriaSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + sub + AniLibria\sound\AniLibria\Gekijouban Fairy Tail Houou no Miko.mka""" }
    val anythingGroupSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[211] Fairy Tail Movie 1.mka""" }
    val sub = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + sub + AniLibria\sub\Fratelli & Timecraft & BW\Gekijouban Fairy Tail Houou no Miko.ass""" }

    val output = Output { path="""K:\temp Fairy Tail Movie 1\Fairy Tail Movie 1 - Houou no Miko.mkv""" }

    val ffmpeg = ffmpeg {
      stream(video1080pAndJapSound).v().n(1) // video 1080p

      stream(anidubSound).a().n(1) // audio AniDUB
      stream(aniLibriaSound).a().n(1) // audio AniLibria
      stream(anythingGroupSound).a().n(1) // audio Anything Group
      stream(video1080pAndJapSound).a().n(1) // audio jap

      stream(sub).s().n(1) // subtitles rus

      output(output)
    }

    println(ffmpeg.buildCommand())
  }

}







data class Input private constructor(val path: Path){


  class BuilderConfig {
    var path: String = ""
  }
  companion object {
    operator fun invoke(build: BuilderConfig.()->Unit) = BuilderConfig()
      .apply(build)
      .let{ this.build(it) }
    fun build(builderConfig: BuilderConfig): Input {
      return Input(Path.of(builderConfig.path).toAbsolutePath())
    }
  }

}


data class Output private constructor(val path: Path){


  class BuilderConfig {
    var path: String = ""
  }
  companion object {
    operator fun invoke(build: BuilderConfig.()->Unit) = BuilderConfig()
      .apply(build)
      .let{ this.build(it) }
    fun build(builderConfig: BuilderConfig): Output {
      return Output(Path.of(builderConfig.path).toAbsolutePath())
    }
  }

}




enum class StreamType(val command: String){
  VIDEO("v"),
  AUDIO("a"),
  SUBTITLE("s"),
  ATTACHMENT("t");

  override fun toString() = command
}
class StreamConfig(var input: Input) {
  var type: StreamType? = null
  var number: Int? = null
  var codec: CodecConfig = CodecConfig()
  var metadata: StreamMetadataConfig = StreamMetadataConfig()

  fun v() = apply {
    type = StreamType.VIDEO
  }
  fun a() = apply {
    type = StreamType.AUDIO
  }
  fun s() = apply {
    type = StreamType.SUBTITLE
  }
  fun at() = apply {
    type = StreamType.ATTACHMENT
  }

  // нумерация с 1
  fun n(number: Int? = null) = apply {
    this.number = number
  }


}
data class Stream(
  val index: Int,
  val input: Input,
  val inputIndex: Int,
  val type: StreamType? = null,
  val number: Int? = null // нумерация с 0
){
  val streamSelectorCommand get() = (type?.let {":${it.command}"} ?: "") + (number?.let {":${it}"} ?: "")
}




enum class CodecType(val command: String){
  COPY("copy"),

  H264("libx264"),
  H265("libx265"), // -c libx265 -preset slow -crf 20
  AV1("libsvtav1"), // -c libsvtav1 -preset 4 -crf 20

  OGG("libvorbis"), // -c libvorbis -b 192k

  // for .mp4
  MOV_TEXT("mov_text"), // -c mov_text
  // for .mkv
  ASS("ass"), // -c ass
  ;

  override fun toString() = command
}
class CodecConfig {
  // all
  var type: CodecType = CodecType.COPY // copy / libx264 / libx265 / libsvtav1 / ...

  // video
  var preset: Preset? = null // 4 / 5 / slow / medium / ...

  // video
  var crf: Int? = null // Constant Rate Factor

  // video & audio
  // normal audio bitrate is 192k
  var bitrate: String? = null // 128k / 192k / 2M / ...
}
data class Codec(
  val streamIndex: Int,
  val type: CodecType,
  val preset: Preset?,
  val crf: Int?,
  val bitrate: String?,
)



enum class PresetType(val command: String) {
  VERYSLOW("veryslow"),
  SLOWER("slower"),
  SLOW("slow"),
  MEDIUM("medium"),
  FAST("fast"),
  FASTER("faster"),
  VERYFAST("veryfast"),
  SUPERFAST("superfast"),
  ULTRAFAST("ultrafast");

  override fun toString() = command
}
data class Preset private constructor(val type: PresetType? = null, val number: Int? = null){
  constructor(type: PresetType) : this(type, null)
  constructor(number: Int) : this(null, number)

  val command get() = type?.command ?: number?.toString() ?: ""
  override fun toString() = command
}



/*
  To delete metadata value just set empty value:
  -metadata title=
  -metadata title=""
 */
class StreamMetadataConfig {
  var isDefault: Boolean? = null
  var isForced: Boolean? = null
  var title: String? = null
  var language: Language? = null
}
enum class Language(
  val command: String // ISO 639 language code
){
  RUSSIAN("rus"),
  ENGLISH("eng"),
  JAPANESE("jpn");

  override fun toString() = command
}
data class StreamMetadata(
  val streamIndex: Int,
  val isDefault: Boolean? = null,
  val isForced: Boolean? = null,
  val title: String? = null,
  val language: Language? = null,
)



class FileMetadataConfig {
  var title: String? = null
}
data class FileMetadata(
  val title: String? = null
)



data class ffmpeg(
  val allCodecsCopy: Boolean,
  val allStreamsNotDefaultNotForced: Boolean,
  val inputs: List<Input>,
  val fileMetadata: FileMetadata,
  val streams: List<Stream>,
  val codecs: List<Codec>,
  val metadata: List<StreamMetadata>,
  val output: Output,
){

  class BuilderConfig {
    var allCodecsCopy = true
    var allStreamsNotDefaultNotForced = true

    val fileMetadata = FileMetadataConfig()

    var streams = mutableListOf<StreamConfig>()
    fun stream(input: Input) = StreamConfig(input).also(streams::add)

    var output: Output? = null
    fun output(output: Output){ this.output = output }
  }

  companion object {
    operator fun invoke(build: BuilderConfig.() -> Unit): ffmpeg {
      val config = BuilderConfig().apply(build)
      return this.build(config)
    }
    fun build(config: BuilderConfig): ffmpeg {
      val inputs = mutableListOf<Input>()
      val fileMetadata = FileMetadata(config.fileMetadata.title)
      val streams = mutableListOf<Stream>()
      val codecs = mutableListOf<Codec>()
      val metadata = mutableListOf<StreamMetadata>()
      config.streams.forEachIndexed { i,s ->
        val inputIndex = inputs.indexOf(s.input).let {
          if (it==-1){
            inputs.add(s.input)
            inputs.lastIndex
          } else it
        }
        val stream = Stream(i, s.input, inputIndex, s.type, s.number?.let { it-1 })
          .also(streams::add)
        Codec(stream.index, s.codec.type, s.codec.preset, s.codec.crf, s.codec.bitrate)
          .also(codecs::add)
        s.metadata.let { StreamMetadata(stream.index, it.isDefault, it.isForced, it.title, it.language) }
          .also(metadata::add)
      }
      val output = config.output ?: throw IllegalArgumentException("Output must be specified")
      return ffmpeg(
        config.allCodecsCopy,
        config.allStreamsNotDefaultNotForced,
        inputs.toList(),
        fileMetadata,
        streams.toList(),
        codecs.toList(),
        metadata.toList(),
        output
      )
    }
  }


  fun buildCommand(): String {
    val command = StringBuilder("ffmpeg ")

    inputs.forEach { command.append("""-i "${it.path}" """) }

    fileMetadata.let {
      it.title?.let { command.append("""-metadata title="$it" """) }
    }

    streams.forEach {
      command.append("-map ${it.inputIndex}${it.streamSelectorCommand} ")
    }

    if (allCodecsCopy) command.append("-c copy ")

    codecs.forEach {
      val index = it.streamIndex
      command.append("-c:${index} ${it.type} ")
      it.preset?.let { command.append("-preset:$index $it ") }
      it.crf?.let { command.append("-crf:$index $it ") }
      it.bitrate?. let { command.append("-b:$index $it ") }
    }

    if (allStreamsNotDefaultNotForced) command.append("-disposition -default-forced ")

    metadata.forEach {
      if (it.isDefault!=null || it.isForced!=null) {
        command.append("-disposition:${it.streamIndex} ")
        command.append(when(it.isDefault){ true -> "+default"; false -> "-default"; null -> "" })
        command.append(when(it.isForced){ true -> "+forced"; false -> "-forced"; null -> "" })
        command.append(" ")
      }
    }

    metadata.forEach {
      val index = it.streamIndex
      it.title?.let { command.append("""-metadata:s:$index title="$it" """) }
      it.language?.let { command.append("-metadata:s:$index language=$it ") }
    }

    command.append(""""${output.path}" """)

    return command.deleteAt(command.lastIndex).toString()
  }


}
