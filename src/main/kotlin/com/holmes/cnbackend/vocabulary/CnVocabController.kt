package com.holmes.cnbackend.vocabulary

import com.holmes.cnbackend.controllers.CnControllerInterface
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CnVocabController : CnControllerInterface {
    override fun createTable() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(CnVocabObj)
        }
    }

    override fun destroyTable() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(CnVocabObj)
        }
    }

    override fun executeAllInserts() {
        val vocabData = CnVocabularyParser()
            .parseStrings() as MutableList<CnVocabData>

        transaction {
            addLogger(StdOutSqlLogger)
            val allVocabsId = CnVocabObj.batchInsert(vocabData) { vocab ->
                this[CnVocabObj.id] = vocab.id
                this[CnVocabObj.hanzi] = vocab.hanzi
                this[CnVocabObj.pinyin] = vocab.pinyin
                this[CnVocabObj.english] = vocab.english
                this[CnVocabObj.tags] = vocab.tags
                this[CnVocabObj.related] = vocab.related
                this[CnVocabObj.hskLevel] = vocab.hskLevel
                this[CnVocabObj.lesson] = vocab.lesson
                this[CnVocabObj.lessonLevel] = vocab.lessonLevel
            }

            println("Inserts inserted: $allVocabsId")
        }
    }

    override fun executeNewInserts() {
        val vocabData = CnVocabularyParser()
            .parseStrings() as MutableList<CnVocabData>
        val existingData = selectAllVocab()
        val newData: MutableList<CnVocabData> = mutableListOf()

        vocabData.forEach { newD ->
            if(!existingData.contains(newD)) {
                newData.add(newD)
            }
        }

        transaction {
            addLogger(StdOutSqlLogger)
            val allVocabsId = CnVocabObj.batchInsert(newData) { vocab ->
                this[CnVocabObj.hanzi] = vocab.hanzi
                this[CnVocabObj.pinyin] = vocab.pinyin
                this[CnVocabObj.english] = vocab.english
                this[CnVocabObj.tags] = vocab.tags
                this[CnVocabObj.related] = vocab.related
                this[CnVocabObj.hskLevel] = vocab.hskLevel
                this[CnVocabObj.lesson] = vocab.lesson
                this[CnVocabObj.lessonLevel] = vocab.lessonLevel
            }
            println("Inserts inserted: $allVocabsId")
        }
    }

    fun selectAllVocab(): MutableList<CnVocabData> {
        val data: MutableList<CnVocabData> = mutableListOf()

        transaction {
            addLogger(StdOutSqlLogger)

            for (it in CnVocabObj.selectAll()) {
                data.add(buildDataFromResult(it))
            }
        }

        return data
    }

    fun selectAllOfLesson(lesson: String?): MutableList<CnVocabData> {
        val data: MutableList<CnVocabData> = mutableListOf()

        transaction {
            addLogger(StdOutSqlLogger)

            CnVocabObj.select {
                CnVocabObj.lesson.lowerCase().eq(lesson?.lowercase())
            }.forEach{
                data.add(buildDataFromResult(it))
            }
        }

        return data
    }

    private fun buildDataFromResult(res: ResultRow): CnVocabData {
        var lesson: String? = null
        if(res[CnVocabObj.lesson]?.isNotBlank() == true) {
            lesson = res[CnVocabObj.lesson]
        }

        return CnVocabData(
            res[CnVocabObj.id],
            res[CnVocabObj.hanzi],
            res[CnVocabObj.pinyin],
            res[CnVocabObj.english],
            res[CnVocabObj.tags],
            res[CnVocabObj.related],
            res[CnVocabObj.hskLevel],
            lesson,
            res[CnVocabObj.lessonLevel]
        )
    }
}