package utilities

import java.nio.file.Path



fun main(){
  extractAudioToOgg()
  //fairyTailOva7Video()
  //kurokoNoBasukeOvaTipOff()
}



private fun extractAudioToOgg() {

  val video = Input { path="""E:\ЗАГРУЗКИ\Kuroko no Basket Ending 2 (Full) Catal Rhythm - Oldcodex.mp4""" }
  val output = Output { path="""E:\ЗАГРУЗКИ\Kuroko no Basket Ending 2 (Full) Catal Rhythm - Oldcodex.ogg""" }

  val ffmpeg = ffmpeg {

    // audio
    stream(video).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "128k"
      }
    }

    output(output)
  }

  println(ffmpeg.buildCommand())
}

private fun fairyTailMovie1Prologue() {
  // Prologue
  // Gekijouban Fairy Tail Prologue Hajimari no Asa

  val video1080pAndJapSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[210] Fairy Tail Movie 1 Sp.mkv""" }
  val anythingGroupSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[210] Fairy Tail Movie 1 Sp.mka""" }
  val aniLibriaSound = Input { path="""K:\temp Fairy Tail Movie 1\Prologue sound AniLibria\Fairy Tail Movie 1 Prologue AniLibria.mp4""" }
  val sub = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + sub + AniLibria\sub\Fratelli & Timecraft & BW\Gekijouban Fairy Tail Prologue Hajimari no Asa.ass""" }

  val movieTitle = "Fairy Tail Movie 1 Prologue - Hajimari no Asa (Утро Зарождения)"
  val output = Output { path="""K:\temp Fairy Tail Movie 1\$movieTitle (1080p, AniLibria, Anything Group, JAP, rus sub).mkv""" }

  val ffmpeg = ffmpeg {
    fileMetadata.apply {
      title = movieTitle
    }

    // video 1080p
    stream(video1080pAndJapSound).v().n(1).apply {
      codec!!.apply{
        type = CodecType.H265
        preset = Preset(PresetType.SLOW)
        crf = 20
      }
      metadata!!.apply {
        title = movieTitle
        language = Language.JAPANESE
        isDefault = true
      }
    }

    // audio Anything Group
    stream(aniLibriaSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "128k"
      }
      metadata!!.apply {
        title = "AniLibria"
        language = Language.RUSSIAN
        isDefault = true
      }
    }

    // audio Anything Group
    stream(anythingGroupSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
      }
      metadata!!.apply {
        title = "Anything Group (Dajana & Sad_Kit)"
        language = Language.RUSSIAN
      }
    }
    // audio jap
    stream(video1080pAndJapSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
      }
      metadata!!.apply {
        title = "Original"
        language = Language.JAPANESE
      }
    }

    // subtitles rus
    stream(sub).s().n(1).apply {
      codec!!.apply {
        type = CodecType.ASS
      }
      metadata!!.apply {
        title = "Fratelli & Timecraft & BW"
        language = Language.RUSSIAN
        isDefault = true
      }
    }

    output(output)
  }

  println(ffmpeg.buildCommand())
}

private fun fairyTailMovie1() {
  // Movie
  // Gekijouban Fairy Tail Houou no Miko

  val video1080pAndJapSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[211] Fairy Tail Movie 1.mkv""" }
  val anidubSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie + AniDUB\[AniDub]_Fairy_Tail_Houou_no_Miko_[Rus]_[DVDRip720p_h264_Ac3]_[MVO].mp4""" }
  val aniLibriaSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + sub + AniLibria\sound\AniLibria\Gekijouban Fairy Tail Houou no Miko.mka""" }
  val anythingGroupSound = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + 1080p + Jap + Anything Group + Menu\[211] Fairy Tail Movie 1.mka""" }
  val sub = Input { path="""K:\temp Fairy Tail Movie 1\Movie & Prologue + sub + AniLibria\sub\Fratelli & Timecraft & BW\Gekijouban Fairy Tail Houou no Miko.ass""" }

  val movieTitle = "Fairy Tail Movie 1 - Houou no Miko (Жрица Феникса)"
  val output = Output { path="""K:\temp Fairy Tail Movie 1\$movieTitle (1080p, AniDUB, AniLibria, Anything Group, JAP, rus sub).mkv""" }

  val ffmpeg = ffmpeg {
    fileMetadata.apply {
      title = movieTitle
    }

    // video 1080p
    stream(video1080pAndJapSound).v().n(1).apply {
      codec!!.apply{
        type = CodecType.H265
        preset = Preset(PresetType.SLOW)
        crf = 20
      }
      metadata!!.apply {
        title = movieTitle
        language = Language.JAPANESE
        isDefault = true
      }
    }

    // audio AniDUB
    stream(anidubSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "AniDUB"
        language = Language.RUSSIAN
        isDefault = true
      }
    }

    // audio AniLibria
    stream(aniLibriaSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "AniLibria"
        language = Language.RUSSIAN
      }
    }

    // audio Anything Group
    stream(anythingGroupSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "Anything Group (Dajana & Sad_Kit)"
        language = Language.RUSSIAN
      }
    }

    // audio jap
    stream(video1080pAndJapSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "Original"
        language = Language.JAPANESE
      }
    }

    // subtitles rus
    stream(sub).s().n(1).apply {
      codec!!.apply {
        type = CodecType.ASS
      }
      metadata!!.apply {
        title = "Fratelli & Timecraft & BW"
        language = Language.RUSSIAN
        isDefault = true
      }
    }

    output(output)
  }

  println(ffmpeg.buildCommand())
}

private fun fairyTailOva7(){
  val video720pAndAncord = Input { path=
    """K:\temp\Fairy Tail (OVA 7) Ancord.mp4"""
  }
  val anythingGroupAndJapAndSubs = Input { path=
    """K:\temp\[212] Fairy Tail OVA 7 серия [Anything Group].mkv"""
  }

  val videoTitle = "Fairy Tail OVA 7"
  val output = Output { path=
    """K:\temp\Fairy Tail s02e028_OVA_7 (720p AniDUB Ancord, Anyting Group, JAP, rus sub).mkv"""
  }

  val ffmpeg = ffmpeg {
    fileMetadata.apply {
      title = videoTitle
    }

    // video 720p
    stream(anythingGroupAndJapAndSubs).v().n(1).apply {
      codec!!.apply {
        type = CodecType.H265
        preset = Preset(PresetType.SLOW)
        crf = 20
      }
      metadata!!.apply {
        title = videoTitle
        language = Language.JAPANESE
        isDefault = true
      }
    }

    // audio AniDUB Ancord
    stream(video720pAndAncord).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
      }
      metadata!!.apply {
        title = "AniDUB Ancord"
        language = Language.RUSSIAN
        isDefault = true
      }
    }

    // audio Anything Group
    stream(anythingGroupAndJapAndSubs).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
      }
      metadata!!.apply {
        title = "Anything Group (Dajana & Sad_Kit)"
        language = Language.RUSSIAN
      }
    }

    // audio jap
    stream(anythingGroupAndJapAndSubs).a().n(2).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
      }
      metadata!!.apply {
        title = "Original"
        language = Language.JAPANESE
      }
    }

    // subtitles rus inscriptions
    stream(anythingGroupAndJapAndSubs).s().n(1).apply {
      codec!!.apply {
        type = CodecType.ASS
      }
      metadata!!.apply {
        title = "Надписи перевод Хаттори"
        language = Language.RUSSIAN
        isDefault = true
      }
    }

    // subtitles rus full
    stream(anythingGroupAndJapAndSubs).s().n(2).apply {
      codec!!.apply {
        type = CodecType.ASS
      }
      metadata!!.apply {
        title = "перевод Хаттори"
        language = Language.RUSSIAN
      }
    }

    output(output)
  }

  println(ffmpeg.buildCommand())
}

private fun fairyTailOva7Video(){
  val video720pAndAncord = Input { path=
    """K:\temp\Fairy Tail (OVA 7) Ancord.mp4"""
  }
  val anythingGroupAndJapAndSubs = Input { path=
    """K:\temp\[212] Fairy Tail OVA 7 серия [Anything Group].mkv"""
  }

  val videoTitle = "Fairy Tail OVA 7"
  val output = Output { path=
    """K:\temp\Fairy Tail s02e028_OVA_7 [1 video].mkv"""
  }

  val ffmpeg = ffmpeg {
    fileMetadata.apply {
      title = videoTitle
    }

    // video 720p
    stream(anythingGroupAndJapAndSubs).v().n(1).apply {
      codec!!.apply {
        type = CodecType.H265
        preset = Preset(PresetType.SLOW)
        resolution = Resolution.HD
        crf = 20
      }
      metadata!!.apply {
        title = videoTitle
        language = Language.JAPANESE
        isDefault = true
      }
    }

    output(output)
  }

  println(ffmpeg.buildCommand())
}



private fun kurokoNoBasukeOvaTipOff() {
  // Movie
  // Gekijouban Fairy Tail Houou no Miko
  
  val video1080pAndEngJapSoundEngSub = Input { path="""M:\Kuroko no Basket Specials\Kuroko's Basketball - S01E22.5 - OVA - Tip Off.mkv""" }
  
  val jamSound = Input { path="""M:\[torrents]\Kuroko no Basuke OVA (JAM 400p)\Kuroko no Basuke [OVA].avi""" }
  val rusSub = Input { path="""M:\[torrents]\Kuroko no Basuke OVA (JAM 400p)\Sub\ASS\Kuroko no Basuke [OVA].ass""" }
  
  val anifilmSound = Input { path="""M:\[torrents]\[Anifilm] Kuroko no Basuke [OVA] [NeaR & Mosa]\[Anifilm] Kuroko no Basuke [OVA] [01 of 01] [1920x1080 x264] [Ru Jp] [NeaR & Mosa].mkv""" }
  
  val movieTitle = "Kuroko Basketball s01e22.5 OVA - Tip Off"
  val output = Output { path="""K:\temp\$movieTitle (1080p, JAM, Anifilm (NeaR & Mosa), Jap, Eng, rus sub, eng sub).mkv""" }
  
  val ffmpeg = ffmpeg {
    fileMetadata.apply {
      title = movieTitle
    }
    
    // video 1080p
    stream(video1080pAndEngJapSoundEngSub).v().n(1).apply {
      codec!!.apply{
        type = CodecType.H265
        preset = Preset(PresetType.SLOW)
        //crf = 20
        crf = 24
      }
      metadata!!.apply {
        title = movieTitle
        language = Language.JAPANESE
        isDefault = true
      }
    }
    
    // audio JAM
    stream(jamSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "Animedia & JAM"
        language = Language.RUSSIAN
        isDefault = true
      }
    }
    
    // audio Anifilm (NeaR & Mosa)
    stream(anifilmSound).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "Anifilm (NeaR & Mosa)"
        language = Language.RUSSIAN
      }
    }
    
    // audio jap
    stream(video1080pAndEngJapSoundEngSub).a().n(2).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "Original"
        language = Language.JAPANESE
      }
    }
    
    // audio Eng
    stream(video1080pAndEngJapSoundEngSub).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "192k"
        channels = 2
      }
      metadata!!.apply {
        title = "Eng"
        language = Language.ENGLISH
      }
    }
    
    // subtitles rus
    stream(rusSub).s().n(1).apply {
      codec!!.apply {
        type = CodecType.ASS
      }
      metadata!!.apply {
        title = "rus (full)"
        language = Language.RUSSIAN
        isDefault = true
      }
    }
    
    // subtitles eng Saizen (full)
    stream(video1080pAndEngJapSoundEngSub).s().n(2).apply {
      codec!!.apply {
        type = CodecType.ASS
      }
      metadata!!.apply {
        title = "eng Saizen (full)"
        language = Language.ENGLISH
      }
    }
    
    // subtitles eng Saizen (signs & songs)
    stream(video1080pAndEngJapSoundEngSub).s().n(1).apply {
      codec!!.apply {
        type = CodecType.ASS
      }
      metadata!!.apply {
        title = "eng Saizen (signs & songs)"
        language = Language.ENGLISH
      }
    }
    
    stream(video1080pAndEngJapSoundEngSub).t()
    stream(anifilmSound).t()
    
    output(output)
  }
  
  println(ffmpeg.buildCommand())
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
  var typeNumber: Int? = null
  var codec: CodecConfig? = CodecConfig()
  var metadata: StreamMetadataConfig? = StreamMetadataConfig()

  fun v() = apply { type = StreamType.VIDEO }
  fun a() = apply { type = StreamType.AUDIO }
  fun s() = apply { type = StreamType.SUBTITLE }
  fun t() = apply {
    type = StreamType.ATTACHMENT
    codec = null
    metadata = null
  }

  // нумерация с 1
  fun n(number: Int? = null) = apply {
    this.typeNumber = number
  }


}
data class Stream(
  val input: Input,
  val inputIndex: Int,
  val type: StreamType? = null,
  val typeNumber: Int? = null, // нумерация с 0
  val outputIndex: Int?,
  val outputTypeIndex: Int?,
){
  val streamInputSelectorCommand get() = (type?.let {":${it.command}"} ?: "") + (typeNumber?.let {":${it}"} ?: "")
  val streamOutputSelectorCommand: String get(){
    if (type!=null && outputTypeIndex!=null) return ":${type.command}:$outputTypeIndex"
    if (outputIndex!=null) return ":$outputIndex"
    throw RuntimeException("Can't select stream $this")
  }
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
  var resolution: Resolution? = null // 1920x1080 / 1280x720 / ...

  // video
  var crf: Int? = null // Constant Rate Factor

  // todo Calculate new bitrate from old
  // video & audio
  // normal audio bitrate is 192k
  var bitrate: String? = null // 128k / 192k / 2M / ...

  // audio
  var channels: Int? = null // 1 / 2 / 6
}
data class Codec(
  val stream: Stream,
  val type: CodecType,
  val preset: Preset?,
  val resolution: Resolution?,
  val crf: Int?,
  val bitrate: String?,
  val channels: Int?,
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
data class Resolution(
  val w: Int,
  val h: Int,
){
  override fun toString() = "${w}:${h}"
  companion object {
    val FHD = Resolution(1920,1080)
    val HD = Resolution(1280,720)
  }
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
  //var handler_name: String? = null // обычно стоит убрать это свойство
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
  val stream: Stream,
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
      var streamNumber: Int? = 0
      val typeStreamNumber: MutableMap<StreamType,Int?> = StreamType.entries.associateWithTo(mutableMapOf()){0}
      config.streams.forEach { s ->
        val inputIndex = inputs.indexOf(s.input).let {
          if (it==-1){
            inputs.add(s.input)
            inputs.lastIndex
          } else it
        }
        if(s.type==null){
          typeStreamNumber.replaceAll { t, u -> null }
        }
        if(s.typeNumber==null) {
          streamNumber = null
          s.type?.let { type -> typeStreamNumber[type] = null }
        }
        val stream = Stream(
          s.input, inputIndex, s.type, s.typeNumber?.let { it-1 },
          streamNumber, typeStreamNumber[s.type]
        ).also(streams::add)
        
        streamNumber = streamNumber?.let { it+1 }
        s.type?.let { type -> typeStreamNumber[type] = typeStreamNumber[type]?.let { it+1 } }
        
        s.codec?.run {
          Codec(stream, type, preset, resolution, crf, bitrate, channels)
        }?.also(codecs::add)
        s.metadata?.run {
          StreamMetadata(stream, isDefault, isForced, title, language)
        }?.also(metadata::add)
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
      command.append("-map ${it.inputIndex}${it.streamInputSelectorCommand} ")
    }

    if (allCodecsCopy) command.append("-c copy ")

    codecs.forEach {
      val idx = it.stream.streamOutputSelectorCommand
      command.append("-c$idx ${it.type} ")
      it.preset?.let { command.append("-preset$idx $it ") }
      it.resolution?.let { command.append("-vf$idx scale=$it ") }
      it.crf?.let { command.append("-crf$idx $it ") }
      it.bitrate?.let { command.append("-b$idx $it ") }
      it.channels?.let { command.append("-ac$idx $it ") }
    }

    if (allStreamsNotDefaultNotForced) command.append("-disposition -default-forced ")

    metadata.forEach {
      if (it.isDefault!=null || it.isForced!=null) {
        val idx = it.stream.streamOutputSelectorCommand
        command.append("-disposition$idx ")
        command.append(when(it.isDefault){ true -> "+default"; false -> "-default"; null -> "" })
        command.append(when(it.isForced){ true -> "+forced"; false -> "-forced"; null -> "" })
        command.append(" ")
      }
    }

    metadata.forEach {
      val idx = it.stream.streamOutputSelectorCommand
      it.title?.let { command.append("""-metadata:s$idx title="$it" """) }
      it.language?.let { command.append("-metadata:s$idx language=$it ") }
    }

    command.append(""""${output.path}" """)

    return command.deleteAt(command.lastIndex).toString()
  }


}
