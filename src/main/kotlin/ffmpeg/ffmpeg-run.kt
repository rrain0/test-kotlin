package ffmpeg


fun main(){
  //extractAudioToOgg320k()
  extractAudio()
  
  //extractMeme()
  
  //fairyTailOva7Video()
  //kurokoNoBasukeOvaTipOff()
  //videoUpscale()
}




private fun videoUpscale() {
  val input = Input { path="""E:\KWORK\кворк 1\работа 2 - Tetris\Tetris 2024-02-06 16-45-05.mp4""" }
  val output = Output { path="""E:\KWORK\кворк 1\работа 2 - Tetris\Tetris 2024-02-06 16-45-05 (2).mp4""" }
  val ffmpeg = ffmpeg {
    
    stream(input).v().n(1).apply {
      codec!!.apply {
        type = CodecType.H264
        resolution = Resolution(960,720)
      }
    }
    
    output(output)
  }
  
  println(ffmpeg.buildCommand())
}



private fun extractAudioToOgg320k() {
  val video = Input { path="""E:\ЗАГРУЗКИ\【GhostFinal】One Hit Kill 「Girls Frontline 2 OST」 【ドールズフロントライン 2】Official.mkv""" }
  val output = Output { path="""E:\ЗАГРУЗКИ\【GhostFinal】One Hit Kill 「Girls Frontline 2 OST」 【ドールズフロントライン 2】Official.ogg""" }
  val ffmpeg = ffmpeg {
    
    // audio
    stream(video).a().n(1).apply {
      codec!!.apply {
        type = CodecType.OGG
        bitrate = "320k"
      }
    }
    
    output(output)
  }
  
  println(ffmpeg.buildCommand())
}

private fun extractAudio() {
  val video = Input { path="""E:\ЗАГРУЗКИ\NOXEK - TAKE FROM ME.mkv""" }
  val output = Output { path="""E:\ЗАГРУЗКИ\NOXEK - TAKE FROM ME.ogg""" }
  val ffmpeg = ffmpeg {
    
    // audio
    stream(video).a().n(1).apply {
      codec!!.apply {
        type = CodecType.COPY
      }
    }
    
    output(output)
  }
  
  println(ffmpeg.buildCommand())
}




private fun extractMeme() {
  
  val video = Input { path="""E:\ЗАГРУЗКИ\достать концовку Встретил бывшую на вебкаме )0.mkv""" }
  
  val output = Output { path="""E:\ЗАГРУЗКИ\meme.mp4""" }
  
  val ffmpeg = ffmpeg {
    fileMetadata.apply {
      title = "Под фонк"
    }
    
    stream(video).v().n(1).apply {
      codec!!.apply{
        type = CodecType.H265
        preset = Preset(PresetType.SLOW)
        crf = 20
      }
    }
    
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
