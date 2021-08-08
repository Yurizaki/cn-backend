package main

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

const val characterDbName: String = "cn_Characters"

object cn_Characters : Table(characterDbName) {
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
    fun createTable() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(cn_Characters)
        }
    }

    fun destroyTable() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(cn_Characters)
        }
    }

    fun getAllCharacters(): MutableList<cn_Char> {
        val data: MutableList<cn_Char> = mutableListOf()

        transaction {
            addLogger(StdOutSqlLogger)

            for (vocabData in cn_Characters.selectAll()) {
                data.add(cn_Char(
                    vocabData[cn_Characters.cn_c_character],
                    vocabData[cn_Characters.cn_c_pinyin]
                ))
            }
        }

        return data
    }
}