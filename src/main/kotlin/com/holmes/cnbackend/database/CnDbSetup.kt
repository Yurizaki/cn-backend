package com.holmes.cnbackend.database

import com.holmes.cnbackend.characters.CnCharacterController
import com.holmes.cnbackend.config.*
import com.holmes.cnbackend.controllers.CnControllerInterface
import com.holmes.cnbackend.vocabulary.CnVocabController
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

var tables: List<CnControllerInterface> = mutableListOf(
    CnVocabController(),
    CnCharacterController()
)

fun dbSetup() {
    var db: Database? = null

    val host = System.getenv(HOST_PROP)
    val user = System.getenv(USER_PROP)
    val pass = System.getenv(PASS_PROP)
    val oper = System.getenv(OPER_PROP)

    println(oper)
    if(host != null && user != null && pass != null && oper != null) {
        db = Database.connect("jdbc:$host$PARAMS_PROP", DRIVER_PROP, user, pass)

        TransactionManager.defaultDatabase = db
        when (oper.lowercase()) {
            PROD_OPER -> {
                tables.forEach {
                    it.destroyTable()
                    it.createTable()
                    it.executeAllInserts()
                }
            }
            TERM_OPER -> {
                tables.forEach {
                    it.destroyTable()
                }
            }
            NOIN_OPER -> {}
            else -> {
                tables.forEach {
                    it.destroyTable()
                }
            }
        }
    }
}
