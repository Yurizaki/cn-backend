package main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

//var db: Database? = null
//
//fun dbStartup() {
//    val host = System.getenv("HEROKU_POSTGRESQL_KOTLIN_HOST")
//    val user = System.getenv("HEROKU_POSTGRESQL_KOTLIN_USER")
//    val pass = System.getenv("HEROKU_POSTGRESQL_KOTLIN_PASS")
//
//    if(host != null) {
//        db = Database.connect("jdbc:$host?sslmode=require", "org.postgresql.Driver", user, pass);
//
//        transaction {
//
//            SchemaUtils.create(cn_Vocabulary)
//
//            val love = cn_Vocabulary.insert {
//                it[cn_character] = "爱"
//                it[cn_pinyin] = "ai"
//                it[cn_translation] = "to love"
//                it[cn_hskLevel] = 1
//            } get cn_Vocabulary.cn_id
//
//            val happy = cn_Vocabulary.insert {
//                it[cn_character] = "高兴"
//                it[cn_pinyin] = "gao xing"
//                it[cn_translation] = "happy"
//                it[cn_hskLevel] = 1
//            } get cn_Vocabulary.cn_id
//
//
//            for (city in cn_Vocabulary.selectAll()) {
//                println(city.toString())
//                println("$city")
//
//                test = city[cn_Vocabulary.cn_character]
//                test2 = city[cn_Vocabulary.cn_pinyin]
//                test3 = city[cn_Vocabulary.cn_translation]
//
//                val numbers = null
//
//
//                myNewList.add(
//                    mutableMapOf (
//                        "id" to "${city[cn_Vocabulary.cn_id]}",
//                        "character" to city[cn_Vocabulary.cn_character],
//                        "pinyin" to city[cn_Vocabulary.cn_pinyin],
//                        "translation" to city[cn_Vocabulary.cn_translation],
//                    )
//                )
//            }
//
//            val tables = db.dialect.allTablesNames()
//            println(tables)
//
//            SchemaUtils.drop (cn_Vocabulary)
//        }
//
//    }
//
//
//}

var test = ""
var test2: String? = null
var test3: String? = null
var myMap: Map<String, String>? = null

var myNewList: MutableList<MutableMap<String, String>> = mutableListOf()

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 23567
    println(port)


    val dbController: CnVocabularyController = CnVocabularyController()

    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing {
            get("") {
                call.respond("I'm alive!")
            }
            get("hello") {
              call.respond(mapOf("vocabulary" to myNewList))
            }
            get("random/{min}/{max}") {
                val min = call.parameters["min"]?.toIntOrNull() ?: 0
                val max = call.parameters["max"]?.toIntOrNull() ?: 10
                val randomString = "${(min until max).shuffled().last()}"
                call.respond(mapOf("value" to randomString))
            }
            get("vocabulary") {
                call.respond(mapOf("data" to dbController.getAllVocabulary()))
            }
        }
    }.start(wait = true)
}