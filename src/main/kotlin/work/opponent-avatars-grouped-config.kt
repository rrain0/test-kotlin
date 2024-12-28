package work.`opponent-avatars-grouped-config`

import com.fasterxml.jackson.databind.ObjectMapper
import com.rrain.util.json.configureJsonPrettier
import java.io.File
import kotlin.uuid.ExperimentalUuidApi


enum class DirType { Level, Gender, RaceHuman, RaceNonHuman }
data class AvaData(
  var level: String = "",
  var gender: String = "",
  var race: String = "",
  var number: Int = 0,
  var pathYc: String = "",
  var pathGp: String = "",
  var pathLocal: String = "",
)

val levels = listOf("beginner", "advanced", "ai")
val genders = listOf("male", "female")
val races = listOf("human", "non-human")

val sourceDir = """F:\SmartBerry\Opponents photo\checkers\OpponentsPhoto"""
val destinationDir = """F:\SmartBerry\Opponents photo\checkers\opponents-photos-grouped"""
val avatars = mutableListOf<AvaData>()

fun main() {
  makeConfig()
}


fun makeConfig() {
  processDir(File(sourceDir), DirType.Level, AvaData())
  
  val jsonObjectMapper = ObjectMapper().configureJsonPrettier()
  val groupCnt: MutableMap<String, MutableMap<String, MutableMap<String, Int>>> = mutableMapOf()
  levels.forEach { l ->
    groupCnt[l] = mutableMapOf()
    genders.forEach { g ->
      groupCnt[l]!![g] = mutableMapOf()
      races.forEach { r ->
        groupCnt[l]!![g]!![r] = 0
      }
    }
  }
  avatars.forEach {
    groupCnt[it.level]!![it.gender]!!.computeIfPresent(it.race) { _, cnt -> cnt + 1 }
  }
  val configContent = jsonObjectMapper.writeValueAsString(groupCnt)
  File("""$destinationDir\opponent-avatars.config.json""").writeText(configContent)
}


fun processDir(file: File, type: DirType, avaData: AvaData) {
  when (type) {
    DirType.Level -> {
      file.listFiles()!!.forEach { file ->
        val level = when (file.name) {
          "1", "beginners" -> "beginner"
          "2-7", "2-17", "medium" -> "advanced"
          "8", "18", "robots" -> "ai"
          else -> null
        }
        level?.let { processDir(file, DirType.Gender, avaData.copy(level = level)) }
      }
    }
    DirType.Gender -> {
      file.listFiles()!!.forEach { file ->
        val gender = when (file.name) {
          "m" -> "male"
          "w" -> "female"
          else -> null
        }
        gender?.let {
          processDir(file, DirType.RaceHuman, avaData.copy(gender = it))
        }
      }
    }
    DirType.RaceHuman -> {
      file.listFiles()!!.forEach { file ->
        if (file.name == "non-human") {
          processDir(file, DirType.RaceNonHuman, avaData)
          return@forEach
        }
        processAvaData(avaData.copy(race = "human"), file)
      }
    }
    DirType.RaceNonHuman -> {
      file.listFiles()!!.forEach { file ->
        processAvaData(avaData.copy(race = "non-human"), file)
      }
    }
  }
}

@OptIn(ExperimentalUuidApi::class)
fun processAvaData(avaData: AvaData, file: File) {
  val d = avaData
  d.number = file.nameWithoutExtension.toInt()
  val avaFileName = "opponent-ava--${
    d.level
  }--${
    d.gender
  }--${
    d.race
  }--${
    d.number.toString().padStart(3, '0')
  }.${
    file.extension
  }"
  d.pathGp = avaFileName
  d.pathYc = avaFileName
  d.pathLocal = avaFileName
  avatars += d
  file.copyTo(File("""$destinationDir\opponents-photos\$avaFileName""").also { it.parentFile.mkdirs() })
}