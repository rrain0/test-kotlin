package test.regexp.`batch-file-rename`

import utils.dot
import java.io.File
import java.nio.file.Files
import java.nio.file.Path





fun main(){


  fun exampleBatchRename(){
    BatchRenameFiles.renameBySeriesNumber(
      """M:\ВИДЕО\[anime]\Tate no Yuusha no Nariagari  Восхождение героя щита\Tate no Yuusha no Nariagari S02 13 eps JAM 1080p""",
      listOf(Regex("""\[JAM\] Tate no Yuusha no Nariagari S02E(?<n>\d{2})( END)? ?\[1080p\]\.mp4""")),
      getOutputName = { """Tate no Yuusha no Nariagari s2e${it.epStr} ${if (it.ep==it.commonInfo!!.lastEp) "END " else ""}JAM 1080p.mp4""" },
      writeNames = false
    )
  }

  BatchRenameFiles.renameBySeriesNumber(
    """L:\[anime]\Ублюдок!! Сокрушитель тьмы (ONA) [DC]  Bastard!! Ankoku no Hakaishin (ONA)""",
    getOutputName = { """BASTARD!! Ankoku no Hakaishin s01e${it.epStr} DC 1080p.mp4""" },
    writeNames = true
  )

}




object BatchRenameFiles {

  fun addExtension(path: String, extension: String){
    File(path).listFiles()?.forEach { f ->
      if (f.isFile) Files.move(f.toPath(), f.toPath().parent.resolve(f.name dot extension))
    }
  }


  // todo make it for season, part, episode S02E17 (S02P2E05)
  // todo get object { season, part, episode }
  class CommonInfo(var firstEp: Int, var lastEp: Int)
  class InfoWithMatch(
    var path: String,
    var name: String,
    var ep: Int? = null,
    var match: MatchResult,
    var commonInfo: CommonInfo? = null,
  )
  open class Info(
    var path: String,
    var name: String,
    var ep: Int? = null,
    var commonInfo: CommonInfo? = null,
    var epStr: String? = null,
    var newName: String? = null
  )
  // overload to remove generics
  fun renameBySeriesNumber(
    containingFolder: String,
    namePatterns: List<Regex> = namesPatternDefault,
    getEpInt: (match: MatchResult)->Int = ::getEpIntDefault,
    getOutputName: (info: Info)->String,
    writeNames: Boolean = false
  ) = renameBySeriesNumber(containingFolder, namePatterns, getEpInt, ::mapToInfoDefault, getOutputName, writeNames)
  fun <T : Info>renameBySeriesNumber(
    containingFolder: String,
    namePatterns: List<Regex> = namesPatternDefault,
    getEpInt: (match: MatchResult)->Int = ::getEpIntDefault,
    mapToInfo: (info: InfoWithMatch)->T,
    getOutputName: (info: T)->String,
    writeNames: Boolean = false
  ){
    val seriesMap = mutableMapOf<Int,InfoWithMatch>()
    File(containingFolder).listFiles()!!.forEach { f ->
      if (f.isFile){
        val name = f.name
        var matchResult: MatchResult? = null
        for (p in namePatterns){
          matchResult = p.matchEntire(name)
          if (matchResult!=null) break
        }

        if (matchResult!=null){
          val info = InfoWithMatch(containingFolder, name, null, matchResult)
          info.ep = getEpInt(info.match)
          if (info.ep in seriesMap) throw RuntimeException("Duplicate series number: ${info.ep}")
          seriesMap[info.ep!!] = info
        }
      }
    }

    val commonInfo = CommonInfo(seriesMap.keys.min(), seriesMap.keys.max())

    val seriesList = seriesMap.values.sortedBy { it.ep }
      .map {
        it.commonInfo = commonInfo
        val info = mapToInfo(it)
        info.epStr = info.epStr ?: epToStrDefault(info.ep!!, commonInfo.lastEp)
        info
      }
      .onEach {
        it.newName = getOutputName(it)
      }

    seriesList.forEach { println("${it.ep} - ${it.name} -> ${it.newName}") }
    println("total: ${seriesList.size}")

    if (writeNames) seriesList.forEach {
      Files.move(Path.of(it.path, it.name), Path.of(it.path, it.newName))
    }
  }
  private val namesPatternDefault = listOf(
    Regex(""".*?[sS](?<s>\d{1,2})[eE](?<n>\d{2,3}).*"""),
    Regex(""".*?(?<n>\d{2,3}).*"""),
  )
  private fun getEpIntDefault(match: MatchResult) = match.groups["n"]!!.value.toInt()
  private fun mapToInfoDefault(info: InfoWithMatch) = Info(info.path, info.name, info.ep, info.commonInfo)
  private fun epToStrDefault(currEp: Int, lastEp: Int) = currEp.toString().padStart(lastEp.toString().length, '0')

}

