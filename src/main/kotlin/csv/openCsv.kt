package csv

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import com.rrain.utils.base.json.createJacksonJsonMapper
import com.rrain.utils.base.print.println
import com.rrain.utils.base.uuid.randomUuid
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.UUID
import kotlin.io.path.Path



fun main() {
  val rows = readCsv()
  processData2(rows)
}



data class Item(val type: String, val name: String)

fun readCsv(): MutableList<Array<String>> {
  val rows = Files.newBufferedReader(
    """src/files/private/Список объектов.csv""".let(::Path),
    Charset.forName("windows-1251")
  ).use {
    val parser = CSVParserBuilder()
      .withSeparator(';')
      //.withIgnoreQuotations(true)
      .build()
    CSVReaderBuilder(it)
      .withSkipLines(0)
      .withCSVParser(parser)
      .build()
      .use { it.readAll() }
  }
  
  // rows.forEach { row ->
  //   println(row.toList())
  // }
  
  return rows
}



private fun processData(rows: MutableList<Array<String>>) {
  
  val driverNames = Files.newBufferedReader("""src/files/private/водители.txt""".let(::Path))
    .readLines()
  
  // Проверять строки на валидность здесь не буду, т.к. на фронт подадутся исходные данные
  val items = rows
    .drop(1)
    .filter { it.size == 2 }
    .map { Item(it[0], it[1]) } +
    driverNames.map { Item("Водитель", it) }
  
  
  println("items:")
  items.forEach { println(it) }
  
  val json = createJacksonJsonMapper().writeValueAsString(items)
  
  Files.newBufferedWriter("""src/files/private/список_объектов.json""".let(::Path)).use {
    it.write(json)
  }
}



data class Item2(
  var id: UUID,
  var name: String,
  var type: String,
  var subtype: String?,
  var data: MutableMap<String, Any>?,
  var is_active: Boolean,
)

private fun processData2(rows: MutableList<Array<String>>) {
  
  val harvesterItems = (1..6).map { Item2(
    id = randomUuid(),
    name = "Комбайн $it",
    type = "machinery",
    subtype = "harvester",
    data = null,
    is_active = true,
  ) }
  
  val rentItems = listOf("fork_lift", "mini_tractor", "tractor", "harvester", "carrier").map { Item2(
    id = randomUuid(),
    name = "Аренда",
    type = "machinery",
    subtype = it,
    data = mutableMapOf("multi" to true),
    is_active = true,
  ) }
  
  val driverNames = Files.newBufferedReader("""src/files/private/водители.txt""".let(::Path))
    .readLines()
  val driverItems = driverNames
    .map { it.trim() }
    .map { Item2(
      id = randomUuid(),
      name = it,
      type = "driver",
      subtype = null,
      data = null,
      is_active = true,
    ) }
  
  val excelItems = rows
    .drop(1)
    .map { listOf(it[0].trim(), it[1].trim()) }
    .filter { it.size == 2 && it[0].isNotBlank() && it[1].isNotBlank() }
    .map { Item2(
      id = randomUuid(),
      name = it[1],
      type = when(it[0]) {
        "Погрузчик" -> "machinery"
        "Мини-трактор" -> "machinery"
        "Трактор" -> "machinery"
        "Комбайн" -> "machinery"
        "Перевозчик" -> "machinery"
        "Водитель" -> "driver"
        "Бригадир" -> "brigade"
        else -> throw RuntimeException("Unknown type: ${it[0]}")
      },
      subtype = when(it[0]) {
        "Погрузчик" -> "fork_lift"
        "Мини-трактор" -> "mini_tractor"
        "Трактор" -> "tractor"
        "Комбайн" -> "harvester"
        "Перевозчик" -> "carrier"
        "Водитель" -> null
        "Бригадир" -> null
        else -> throw RuntimeException("Unknown type: ${it[0]}")
      },
      data = null,
      is_active = true,
    ) }
    
  val items = (harvesterItems + rentItems + driverItems + excelItems)
    .filter {
      it.subtype == "harvester" ||
        it.name.matches(Regex("""(^аренда)""", RegexOption.IGNORE_CASE)) ||
        //it.subtype == "mini_tractor" ||
        it.type == "brigade" ||
        it.type == "driver"
    }
    .onEach {
      if (it.name.matches(Regex("""(^ип\s+\S+$)|(^студенты)|(^аренда)""", RegexOption.IGNORE_CASE))) {
        it.data = (it.data ?: mutableMapOf()).also {
          it["multi"] = true
        }
      }
      // if (it.type == "brigade") {
      //   it.data = (it.data ?: mutableMapOf()).also {
      //     it["workers_cnt"] = 0
      //   }
      // }
    }
  
  
  println("items:")
  items.forEach { println(it) }
  
  val json = createJacksonJsonMapper().writeValueAsString(items)
  
  // Теперь надо вычитывать сначала то что есть чтобы сохранить uuid.
  Files.newBufferedWriter("""src/files/private/hardcoded-units.json""".let(::Path)).use {
    it.write(json)
  }
}
