package main

import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import io.ktor.freemarker.*

private var db: Database? = null
val cnVocabularyController: CnVocabularyController = CnVocabularyController()
val cnCharacterController: CnCharacterController = CnCharacterController()
val cnVocabularyInserts: CnVocabularyInserts = CnVocabularyInserts()
val cnCharacterInserts: CnCharacterInserts = CnCharacterInserts()

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
    }.start(wait = true)
}