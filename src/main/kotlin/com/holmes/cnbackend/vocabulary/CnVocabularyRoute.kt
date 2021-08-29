package com.holmes.cnbackend.vocabulary

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getVocabulary(routeName: String) {
    get(routeName) {

        if (call.request.queryParameters.isEmpty()) {
            call.respond(mapOf("data" to CnVocabController().selectAllVocab()))
        }
        else if (call.request.queryParameters.contains("lesson")) {
            val lesson = call.request.queryParameters["lesson"]

            call.respond(mapOf("data" to CnVocabController().selectAllOfLesson(lesson)))
        }
    }
}
