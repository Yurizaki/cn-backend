package com.holmes.cnbackend.vocabulary

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getVocabulary(routeName: String) {
    get(routeName) {
        call.respond(mapOf("data" to CnVocabularyController().getAllVocabulary()))
    }
}
