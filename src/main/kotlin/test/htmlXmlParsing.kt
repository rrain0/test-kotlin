package test

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser

fun main(){
    xmlParsing()
}

private fun xmlParsing(){
    run {
        val text = "some simple plain text"
        println("source text:")
        println(text)

        val document = Jsoup.parse(text, "", Parser.xmlParser())

        println("document.toString():")
        println(document)
    }

    println()
    println()

    // Оборачивание single node
    run {
        val text = "some <i>simple</i> plain text"
        println("source text:")
        println(text)

        val document = Jsoup.parse(text, "", Parser.xmlParser())

        println("child 0: ${document.child(0)}") // <i>simple</i>
        println("document.text(): ${document.text()}") // some simple plain text
        println("document.children(): ${document.children()}") // <i>simple</i>
        println("document.ownText(): ${document.ownText()}") // some plain text
        println("document.textNodes(): ${document.textNodes()}") // [some ,  plain text]

        document.textNodes().forEach { it.wrap("<p></p>") }

        println("document.toString():")
        // after text wrapping
        println(document) // <p>some </p><i>simple</i><p> plain text</p>
    }

    println()
    println()

    // Замена одного тега другим
    run {
        val text = """
            |lorem <i>ipsum</i> sit
            |<article-image localId="3" />
            |amet
        """.trimMargin()
        println("source text:")
        println(text)

        val document = Jsoup.parse(text, "", Parser.xmlParser())

        val elements = document.select("article-image")
        elements.forEach {
            // <img src="localId=2" style="display: block; width: 100%; height: 300px; object-fit: cover;"/>
            val img = Jsoup.parseBodyFragment("""<img src="localId=${it.attr("localId")}" style="display: block; width: 100%; height: 300px; object-fit: cover;"/>""")
                .select("img")[0]
            it.replaceWith(img)
        }

        println("document.toString():")
        println(document)
    }

    // todo Оборачивание нескольких детей одним тегом
}