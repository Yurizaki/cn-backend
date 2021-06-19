package main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.HttpStatusCode
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

var db: Database? = null

object cn_Vocabulary : Table() {
    val cn_id = integer("cn_Id").autoIncrement()
    val cn_character = varchar("cn_character", 10)
    val cn_pinyin = varchar("cn_pinyin", 60)
    val cn_translation = varchar("cn_translation", 100)
    val cn_tags = varchar("cn_tags", 100).nullable()
    val cn_related = varchar("cn_related", 100).nullable()
    val cn_hskLevel = integer("cn_hskLevel")

    override val primaryKey = PrimaryKey(cn_id, name = "PK_Vocabulary_ID")
}

fun dbStartup() {
    val host = System.getenv("HEROKU_POSTGRESQL_KOTLIN_HOST")
    val user = System.getenv("HEROKU_POSTGRESQL_KOTLIN_USER")
    val pass = System.getenv("HEROKU_POSTGRESQL_KOTLIN_PASS")

    if(host != null) {
        db = Database.connect("jdbc:$host?sslmode=require", "org.postgresql.Driver", user, pass);

        //    val db2 = Database.connect("jdbc:pgsql://ec2-52-7-115-250.compute-1.amazonaws.com:5432/d9fdbflj7g1t8a?ssl.mode=Require",
//        driver = "com.impossibl.postgres.jdbc.PGDriver",
//        user = "iebyhqwbwatafi",
//        password = "7ab90e0fc97e192d4feca6f74c741acfa43d8cba33c685f031a984d75036a6f0")

        transaction {

            SchemaUtils.create(cn_Vocabulary)

            val love = cn_Vocabulary.insert {
                it[cn_character] = "爱"
                it[cn_pinyin] = "ai"
                it[cn_translation] = "to love"
                it[cn_hskLevel] = 1
            } get cn_Vocabulary.cn_id

            val happy = cn_Vocabulary.insert {
                it[cn_character] = "高兴"
                it[cn_pinyin] = "gao xing"
                it[cn_translation] = "happy"
                it[cn_hskLevel] = 1
            } get cn_Vocabulary.cn_id


            for (city in cn_Vocabulary.selectAll()) {
                println(city.toString())
                println("$city")

                test = city[cn_Vocabulary.cn_character]
                test2 = city[cn_Vocabulary.cn_pinyin]
                test3 = city[cn_Vocabulary.cn_translation]

                val numbers = null


                myNewList.add(
                    mutableMapOf (
                        "id" to "${city[cn_Vocabulary.cn_id]}",
                        "character" to city[cn_Vocabulary.cn_character],
                        "pinyin" to city[cn_Vocabulary.cn_pinyin],
                        "translation" to city[cn_Vocabulary.cn_translation],
                    )
                )
            }

            SchemaUtils.drop (cn_Vocabulary)
        }

    }


}

var test = ""
var test2: String? = null
var test3: String? = null
var myMap: Map<String, String>? = null

var myNewList: MutableList<MutableMap<String, String>> = mutableListOf()

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 23567
    println(port)

    dbStartup()

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

                val env = System.getenv("DATABASE_URL");
                val props = Properties()
                val env2 = props.getProperty("DATABASE_URL")
//
//                println("================:${env}:")
//                println("================:${props.getProperty("myprop")}:")
//
//                prop.stringPropertyNames()
//                    .associateWith {prop.getProperty(it)}
//                    .forEach { println(it) }

                  call.respond(mapOf("vocabulary" to myNewList))

//                call.respond(HttpStatusCode.Accepted, "Hello + $env + $env2 + $test")
            }
            get("random/{min}/{max}") {
                val min = call.parameters["min"]?.toIntOrNull() ?: 0
                val max = call.parameters["max"]?.toIntOrNull() ?: 10
                val randomString = "${(min until max).shuffled().last()}"
                call.respond(mapOf("value" to randomString))
            }
        }
    }.start(wait = true)
}