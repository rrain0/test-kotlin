package scripts.`opponent-avatars-flat-config`

import com.fasterxml.jackson.databind.ObjectMapper
import com.rrain.utils.base.json.configureJsonPrettier
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid



enum class DirType { Level, Gender, RaceHuman, RaceNonHuman }
data class AvaData(
  var id: String = "",
  var gender: String = "",
  var race: String = "",
  var level: String = "",
  var pathYc: String = "",
  var pathGp: String = "",
  var pathLocal: String = "",
)


val sourceDir = """F:\SmartBerry\Opponents photo\arena-games\OpponentsPhoto"""
val destinationDir = """F:\SmartBerry\Opponents photo\arena-games\opponents-photos-flat"""
val avatars = mutableListOf<AvaData>()

fun main() {
  makeFlatConfig()
}


fun makeFlatConfig() {
  processDir(File(sourceDir), DirType.Level, AvaData())
  
  val jsonObjectMapper = ObjectMapper().configureJsonPrettier()
  val configContent = jsonObjectMapper.writeValueAsString(avatars)
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
  d.id = Uuid.random().toString()
  val avaFileName = "opponent-ava--${d.level}--${d.gender}--${d.race}--${d.id}.${file.extension}"
  d.pathGp = avaFileName
  d.pathYc = avaFileName
  d.pathLocal = avaFileName
  avatars += d
  file.copyTo(File("""$destinationDir\opponents-photos\$avaFileName""").also { it.parentFile.mkdirs() })
}