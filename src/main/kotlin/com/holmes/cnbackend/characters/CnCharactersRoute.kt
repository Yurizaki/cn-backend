package com.holmes.cnbackend.characters

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getCharacters(routeName: String) {
    get(routeName) {
        call.respond(mapOf("data" to CnCharacterController().getAllCharacters()))
    }
}

