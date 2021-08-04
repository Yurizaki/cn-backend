package main

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

val characterDbName: String = "cn_Characters"

object cn_Characters : Table("$characterDbName") {
    val cn_c_id           = integer("cn_Id").autoIncrement()
    val cn_c_character    = varchar("cn_character", 10)
    val cn_c_pinyin       = varchar("cn_pinyin", 60)

    override val primaryKey = PrimaryKey(cn_c_id, name = "PK_Character_ID")
}

data class cn_Char(
    val cn_character    : String,
    val cn_pinyin       : String,
)

class CnCharacterController {

    private var db: Database? = null

    init {
        setupConnection()
    }

    private fun setupConnection() {
        val host = System.getenv("HEROKU_POSTGRESQL_KOTLIN_HOST")
        val user = System.getenv("HEROKU_POSTGRESQL_KOTLIN_USER")
        val pass = System.getenv("HEROKU_POSTGRESQL_KOTLIN_PASS")
        val operation = System.getenv("HEROKU_POSTGRESQL_KOTLIN_OPERATION")

        if(host != null && user != null && pass != null) {
            db = Database.connect("jdbc:$host?sslmode=require&reWriteBatchedInserts=true", "org.postgresql.Driver", user, pass)
            TransactionManager.defaultDatabase = db

            when (operation) {
                "prod" -> {
                    initialiseDb()
                    executeAllVocabularyInserts()
                }
                "closeDown" -> {
                    destroyDb()
                }
                "dev" -> {
                    initialiseDb()
                    executeAllVocabularyInserts()
                    destroyDb()
                }

            }
        }
    }

    private fun initialiseDb() {
        transaction {
            addLogger(StdOutSqlLogger)
            val tables = db.dialect.allTablesNames()
            SchemaUtils.create(cn_Vocabulary)
        }
    }

    private fun destroyDb() {
        transaction {
            addLogger(StdOutSqlLogger)
            val tables = db.dialect.allTablesNames()

            SchemaUtils.drop(cn_Vocabulary)
        }
    }

    fun getAllVocabulary(): MutableList<cn_Vocab> {
        val data: MutableList<cn_Vocab> = mutableListOf()

        transaction {
            addLogger(StdOutSqlLogger)

            for (vocabData in cn_Vocabulary.selectAll()) {
                data.add(cn_Vocab(
                    vocabData[cn_Vocabulary.cn_character],
                    vocabData[cn_Vocabulary.cn_pinyin],
                    vocabData[cn_Vocabulary.cn_translation],
                    vocabData[cn_Vocabulary.cn_tags],
                    vocabData[cn_Vocabulary.cn_related],
                    vocabData[cn_Vocabulary.cn_hskLevel]
                ))
            }
        }

        return data
    }

}