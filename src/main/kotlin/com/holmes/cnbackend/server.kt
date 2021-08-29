package com.holmes.cnbackend

import com.holmes.cnbackend.characters.getCharacters
import com.holmes.cnbackend.config.ROUTE_CHARS
import com.holmes.cnbackend.config.ROUTE_VOCAB
import com.holmes.cnbackend.config.SERV_PORT
import com.holmes.cnbackend.database.dbSetup
import com.holmes.cnbackend.routes.getPostAdmin
import com.holmes.cnbackend.routes.getScrape
import com.holmes.cnbackend.vocabulary.getVocabulary
import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.content.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.p
import java.io.File

val loadedRoutes : MutableList<String> = mutableListOf()

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: SERV_PORT
    dbSetup()

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
        install(StatusPages) {
            status(HttpStatusCode.NotFound) {
                call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8), it))
            }
        }

        routing {
            get("/") {
                call.respondHtml {
                    body {
                        h1 {
                            +"Application running on routes:"
                        }
                        loadedRoutes.forEach {
                            a("/$it") {
                                p {
                                    +it
                                }
                            }
                        }
                    }
                }
            }
        }
        routing {
            getVocabulary(ROUTE_VOCAB)
            loadedRoutes.add(ROUTE_VOCAB)
        }
        routing {
            getCharacters(ROUTE_CHARS)
            loadedRoutes.add(ROUTE_CHARS)
        }
        routing {
            getPostAdmin()
        }
        routing {
            getScrape()
        }
    }.start(wait = true)
}