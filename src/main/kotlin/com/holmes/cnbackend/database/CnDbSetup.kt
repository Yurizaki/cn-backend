package com.holmes.cnbackend.database

import com.holmes.cnbackend.characters.CnCharacterController
import com.holmes.cnbackend.config.*
import com.holmes.cnbackend.controllers.CnControllerInterface
import com.holmes.cnbackend.vocabulary.CnVocabController
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import kotlin.system.exitProcess

var tables: Map<String, CnControllerInterface> = mutableMapOf(
    DB_VOCAB to CnVocabController()
)

fun dbSetup() {
    var db: Database? = null

    val host = System.getenv(HOST_PROP)
    val user = System.getenv(USER_PROP)
    val pass = System.getenv(PASS_PROP)
    val oper = System.getenv(OPER_PROP)

    if(host != null && user != null && pass != null && oper != null) {
        db = Database.connect("jdbc:$host$PARAMS_PROP", DRIVER_PROP, user, pass)

        TransactionManager.defaultDatabase = db
        when (oper.lowercase()) {
            PROD_OPER -> {
                tables.forEach {
                    println("Executing [$PROD_OPER] operation for table [${it.key}].")
                    it.value.destroyTable()
                    it.value.createTable()
                    it.value.executeAllInserts()
                    println("Operation [$PROD_OPER] for table [${it.key}] complete.")
                }
            }
            UPDT_OPER -> {
                tables.forEach {
                    println("Executing [$PROD_OPER] operation for table [${it.key}].")
                    it.value.executeNewInserts()
                    println("Operation [$PROD_OPER] for table [${it.key}] complete.")
                }
            }
            TERM_OPER -> {
                tables.forEach {
                    println("Executing [$PROD_OPER] operation for table [${it.key}].")
                    it.value.destroyTable()
                    println("Operation [$PROD_OPER] for table [${it.key}] complete.")
                }

                exitProcess(1)
            }
            NOIN_OPER -> {}
            else -> {
                tables.forEach {
                    println("Executing [$PROD_OPER] operation for table [${it.key}].")
                    it.value.destroyTable()
                    println("Operation [$PROD_OPER] for table [${it.key}] complete.")
                }
            }
        }
    }
}
