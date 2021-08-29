package com.holmes.cnbackend.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jsoup.Jsoup

data class ScrapedResult (
    val hanzi: String,
    val pinyin: String
)

fun Route.getScrape() {
    get("/scrape/{p}") {
        val par = call.parameters["p"]
        val myL : MutableList<ScrapedResult> = mutableListOf()
        val doc = Jsoup.connect("https://www.mdbg.net/chinese/dictionary?wdqb=${par}").get()    // <1>
        doc.select(".resultswrap")
            .select("tbody")
            .select(".head")

            .parallelStream()
            .filter { it != null }
            .forEach {

                var hanzi = it.select(".hanzi")
                    .select("span").text()
                println(hanzi)

                var pinyin = it.select(".pinyin")
                    .select("span").text()
                println(pinyin)


                myL.add(ScrapedResult(hanzi, pinyin))
            }

        call.respond(mapOf("data" to myL))
    }
}

