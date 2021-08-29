package com.holmes.cnbackend.vocabulary

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

class CnVocabularyInserts {
    fun executeAllVocabularyInserts() {
        val hskLevel: Int = 1
        val pnTag: String = "pronoun"
        val vbTag: String = "verb"
        val adjTag: String = "adjective"


        val vocabMap: MutableList<cn_Vocab> = mutableListOf()
        vocabMap.add(cn_Vocab("爱",   "ài",  "to love", vbTag,   null, hskLevel))
        vocabMap.add(cn_Vocab("你",   "nǐ",  "you",     pnTag,   null, hskLevel))
        vocabMap.add(cn_Vocab("好",   "hǎo", "good",    adjTag,  null, hskLevel))


        vocabMap.add(cn_Vocab("高兴", "gao xing", "happy", null, null, hskLevel))
        vocabMap.add(cn_Vocab("你好", "nǐ hǎo",   "hello", null, null, hskLevel))

        transaction {
            addLogger(StdOutSqlLogger)
            val allVocabsId = cn_Vocabulary.batchInsert(vocabMap) { vocab ->
                this[cn_Vocabulary.cn_character] = vocab.cn_character
                this[cn_Vocabulary.cn_pinyin] = vocab.cn_pinyin
                this[cn_Vocabulary.cn_translation] = vocab.cn_translation
                this[cn_Vocabulary.cn_tags] = vocab.cn_tags
                this[cn_Vocabulary.cn_related] = vocab.cn_related
                this[cn_Vocabulary.cn_hskLevel] = vocab.cn_hskLevel
            }

            println("Inserts inserted: $allVocabsId")
        }
    }
}