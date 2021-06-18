package main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.*
import java.util.*

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 23567
    println(port)

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


                call.respond(HttpStatusCode.Accepted, "Hello + $env + $env2")
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