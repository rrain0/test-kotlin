package test.regexp

import java.io.File
import java.nio.file.Files

fun main(){
    //regexp()
    regexBatchRenameFiles()
}

fun regexp(){

    var someIp = "192.198.3.5"

    val ipPattern = Regex("""(?<n1>\d{1,3})\.(?<n2>\d{1,3})\.(?<n3>\d{1,3})\.(?<n4>\d{1,3})""")

    var matchResult = ipPattern.matchEntire(someIp)
    if (matchResult != null){
        val matchGroupsMap = mapOf(
            "n1" to matchResult.groups["n1"],
            "n2" to matchResult.groups["n2"],
            "n3" to matchResult.groups["n3"],
            "n4" to matchResult.groups["n4"],
        )

        println(matchGroupsMap)


        val map = mapOf(
            "n1" to matchResult.groups["n1"]!!.value,
            "n2" to matchResult.groups["n2"]!!.value,
            "n3" to matchResult.groups["n3"]!!.value,
            "n4" to matchResult.groups["n4"]!!.value,
        )

        println(map)
    }


    someIp = "192.198.*.5"
    matchResult = ipPattern.matchEntire(someIp)
    println(matchResult==null)

    run {
        val pattern = Regex("""_[\da-zA-Z]""")
        println("place_sub_type_0a: ${pattern.replace("place_sub_type_0a",{ mr -> mr.value[1].uppercase() })}")
    }

}


fun regexBatchRenameFiles(){

    fun tenseiShiraraSlimeDattaKen(){
        val patterns = listOf(
            Regex("""\[JAM_CLUB]_Tensei_Shitara_Slime_Datta_Ken_(?<n>\d{2})_\[1080p]\[RUS]\[DUB]\.mp4"""),
            Regex("""Tensei Shitara Slime Datta Ken - (?<n>\d{2}).*\.mp4""", RegexOption.IGNORE_CASE),
        )
        File("L:\\ВИДЕО\\О моём перерождении в слизь\\[03] 2 сезон")
            .listFiles()!!.forEach { f ->
                val fName = f.name
                var matchResult: MatchResult? = null
                for (p in patterns){
                    matchResult = p.matchEntire(fName)
                    if (matchResult!=null) break
                }

                if (matchResult!=null){
                    val ep = matchResult!!.groups["n"]!!.value.toInt()-24
                    val part = (ep-1)/12+1
                    val partEp = (ep-1)%12+1
                    val newName = "Tensei Shitara Slime Datta Ken S02E${ep.toString().padStart(2,'0')} (S02P${part}E${partEp.toString().padStart(2,'0')}) [JAM CLUB][1080p][RUS][DUB].mp4"
                    println("old: $fName")
                    println("new: $newName")
                    Files.move(f.toPath(), f.toPath().parent.resolve(newName))
                }
            }
    }

    fun nanatsuNoTaizai(){
        val patterns = listOf(
            Regex("""Яростное правосудие \((?<n>\d{2})\)\.mkv"""),
        )
        File("J:\\ВИДЕО\\Семь Смертных Грехов  Nanatsu No Taizai\\[06] S04 Суд Дракона  Fundo no Shinpan\\Семь смертных грехов - Яростное правосудие (2021 WEBRip) SHIZA Project, AniDub")
            .listFiles()!!.forEach { f ->
                val fName = f.name
                var matchResult: MatchResult? = null
                for (p in patterns){
                    matchResult = p.matchEntire(fName)
                    if (matchResult!=null) break
                }

                if (matchResult!=null){
                    val ep = matchResult!!.groups["n"]!!.value.toInt()
                    val newName = "Nanatsu no Taizai Fundo no Shinpan ${ep.toString().padStart(2,'0')} (2021) [WEBRip 1080p][SHIZA Project, AniDub].mkv"
                    println("old: $fName")
                    println("new: $newName")
                    Files.move(f.toPath(), f.toPath().parent.resolve(newName))
                }
            }
    }

    fun addExtension(){
        File("J:\\ВИДЕО\\Семь Смертных Грехов  Nanatsu No Taizai\\[06] S04 Суд Дракона  Fundo no Shinpan\\Семь смертных грехов - Яростное правосудие (2021 WEBRip) SHIZA Project, AniDub")
            .listFiles()!!.forEach { f ->
                Files.move(f.toPath(), f.toPath().parent.resolve(f.name+".mkv"))
            }
    }

    fun kimetsuNoYaiba(){
        val map = mutableMapOf<String,String>()

        val nameFrom = Regex("""(?<n>\d{2}).(?<name>.*)\.mkv""")
        File("""E:\ТОРРЕНТЫ\Kimetsu.no.Yaiba.s01.2019.BDRip.1080p""")
            .listFiles()!!.forEach { f ->
                val fName = f.name
                val matchResult = nameFrom.matchEntire(fName)!!
                map.put(matchResult.groups["n"]!!.value, matchResult.groups["name"]!!.value.replace('.',' '))
            }

        //println(map)

        val patterns = listOf(
            Regex("""\[Moozzi2] Kimetsu no Yaiba - (?<n>\d{2}) \(BD 1920x1080 x\.265-10Bit FLAC\)\.mkv"""),
        )
        File("""J:\ВИДЕО\Клинок Рассекающий Демонов  Kimetsu no Yaiba\[01] S1\[Moozzi2] Kimetsu no Yaiba [BD 1080p HEVC-10Bit FLAC][Wakanim]""")
            .listFiles()!!.forEach { f ->
                val fName = f.name
                var matchResult: MatchResult? = null
                for (p in patterns){
                    matchResult = p.matchEntire(fName)
                    if (matchResult!=null) break
                }

                if (matchResult!=null){
                    val epInt = matchResult!!.groups["n"]!!.value.toInt()
                    val ep = epInt.toString().padStart(2,'0')
                    val newName = "[Moozzi2] Kimetsu no Yaiba $ep - ${map[ep]} [BD 1080p HEVC-10Bit FLAC][Wakanim].mkv"
                    println("old: $fName")
                    println("new: $newName")
                    Files.move(f.toPath(), f.toPath().parent.resolve(newName))
                }
            }
    }

    //tenseiShiraraSlimeDattaKen()
    //nanatsuNoTaizai()
    //addExtension()
    //kimetsuNoYaiba()

}