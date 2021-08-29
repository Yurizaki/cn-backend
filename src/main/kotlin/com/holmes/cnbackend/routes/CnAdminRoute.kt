package com.holmes.cnbackend.routes

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.body
import kotlinx.html.h1

data class BlogEntry(val headline: String, val body: String)
val blogEntries = mutableListOf(BlogEntry(
    "The drive to develop!",
    "...it's what keeps me going."
))

fun Route.getPostAdmin() {
    get("/admin") {
        call.respond(FreeMarkerContent("index.ftl", mapOf("entries" to blogEntries), ""))
    }

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

