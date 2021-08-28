package main

import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import io.ktor.freemarker.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import it.skrape.core.*
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.DocElement
import it.skrape.selects.eachHref
import it.skrape.selects.eachText
import it.skrape.selects.html5.*
import kotlinx.html.*
import kotlinx.html.td
import org.jsoup.Jsoup

private var db: Database? = null
val cnVocabularyController: CnVocabularyController = CnVocabularyController()
val cnCharacterController: CnCharacterController = CnCharacterController()
val cnVocabularyInserts: CnVocabularyInserts = CnVocabularyInserts()
val cnCharacterInserts: CnCharacterInserts = CnCharacterInserts()

data class ScrapedResult (
    val hanzi: String,
    val pinyin: String
)

data class MySimpleDataClass(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val allParagraphs: List<DocElement>
)

fun initialiseSetup() {
    val host = System.getenv("HEROKU_POSTGRESQL_KOTLIN_HOST")
    val user = System.getenv("HEROKU_POSTGRESQL_KOTLIN_USER")
    val pass = System.getenv("HEROKU_POSTGRESQL_KOTLIN_PASS")
    val operation = System.getenv("HEROKU_POSTGRESQL_KOTLIN_OPERATION")

    if(host != null && user != null && pass != null) {
        db = Database.connect("jdbc:$host?sslmode=require&reWriteBatchedInserts=true", "org.postgresql.Driver", user, pass)

        TransactionManager.defaultDatabase = db
        when (operation) {
            "prod" -> {
                cnVocabularyController.createTable()
                cnCharacterController.createTable()

                cnVocabularyInserts.executeAllVocabularyInserts()
                cnCharacterInserts.executeAllCharacterInserts()
            }
            "closeDown" -> {
                cnVocabularyController.destroyTable()
                cnCharacterController.destroyTable()
            }
            "dev" -> {
                cnVocabularyController.createTable()
                cnCharacterController.createTable()

                cnVocabularyInserts.executeAllVocabularyInserts()
                cnCharacterInserts.executeAllCharacterInserts()

                cnVocabularyController.destroyTable()
                cnCharacterController.destroyTable()
            }
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 23567
    println(port)
    initialiseSetup();

    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }

        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
            outputFormat = HTMLOutputFormat.INSTANCE
        }

        data class BlogEntry(val headline: String, val body: String)
        val blogEntries = mutableListOf(BlogEntry(
            "The drive to develop!",
            "...it's what keeps me going."
        ))

        routing {
            get("vocabulary") {
                call.respond(mapOf("data" to cnVocabularyController.getAllVocabulary()))
            }
        }
        routing {
            get("characters") {
                call.respond(mapOf("data" to cnCharacterController.getAllCharacters()))
            }
        }
        routing {
            get("/admin") {
                call.respond(FreeMarkerContent("index.ftl", mapOf("entries" to blogEntries), ""))
            }
        }
        routing {
            post("/submit") {
                val params = call.receiveParameters()
                val headline = params["headline"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val body = params["body"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                // TODO: send a status page to the user

                println("$headline :: $body")

                call.respondHtml {
                    body {
                        h1 {
                            +"Thanks for submitting your entry!"
                        }
                    }
                }
            }
        }
        routing {
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

//                val extracted = skrape(HttpFetcher) {
//                    request {
//                        url = "https://www.mdbg.net/chinese/dictionary?wdqb=%E6%88%91"
//                    }
//                    response {
//                        MySimpleDataClass(
//                            httpStatusCode = status { code },
//                            httpStatusMessage = status { message },
//                            allParagraphs = document.td {
//                                withClass = "resultswrap"
//                                findAll {
//                                    flatMap {
//                                        it.children
//                                    }
//                                }
//                            }
//                        )
//                    }
//                }
//                print(extracted)

            }
        }
    }.start(wait = true)
}