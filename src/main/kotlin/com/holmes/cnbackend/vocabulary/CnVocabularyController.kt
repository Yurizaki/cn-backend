package com.holmes.cnbackend.vocabulary

import com.holmes.cnbackend.controllers.CnControllerInterface
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

const val vocabularyDbName: String = "cn_Vocabulary"

object cn_Vocabulary : Table(vocabularyDbName) {
    val cn_id           = integer("cn_Id").autoIncrement()
    val cn_character    = varchar("cn_character", 10)
    val cn_pinyin       = varchar("cn_pinyin", 60)
    val cn_translation  = varchar("cn_translation", 100)
    val cn_tags         = varchar("cn_tags", 100).nullable()
    val cn_related      = varchar("cn_related", 100).nullable()
    val cn_hskLevel     = integer("cn_hskLevel")

    override val primaryKey = PrimaryKey(cn_id, name = "PK_Vocabulary_ID")
}

data class cn_Vocab(
    val cn_character    : String,
    val cn_pinyin       : String,
    val cn_translation  : String,
    val cn_tags         : String?,
    val cn_related      : String?,
    val cn_hskLevel     : Int
)

class CnVocabularyController : CnControllerInterface {
    override fun createTable() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(cn_Vocabulary)
        }
    }

    override fun destroyTable() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(cn_Vocabulary)
        }
    }

    override fun executeAllInserts() {
        CnVocabularyInserts().executeAllVocabularyInserts()
    }

    fun getAllVocabulary(): MutableList<cn_Vocab> {
        val data: MutableList<cn_Vocab> = mutableListOf()

        transaction {
            addLogger(StdOutSqlLogger)

            for (vocabData in cn_Vocabulary.selectAll()) {
                data.add(
                    cn_Vocab(
                    vocabData[cn_Vocabulary.cn_character],
                    vocabData[cn_Vocabulary.cn_pinyin],
                    vocabData[cn_Vocabulary.cn_translation],
                    vocabData[cn_Vocabulary.cn_tags],
                    vocabData[cn_Vocabulary.cn_related],
                    vocabData[cn_Vocabulary.cn_hskLevel]
                )
                )
            }
        }

        return data
    }
}