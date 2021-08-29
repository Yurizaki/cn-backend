package com.holmes.cnbackend.vocabulary

@Deprecated("Import via CnVocabularyParser instead. (CSV parsing)")
class CnVocabularyInserts {
    @Deprecated("Import via CnVocabularyParser instead. (CSV parsing)")
    data class cn_Vocab(
        val cn_character    : String,
        val cn_pinyin       : String,
        val cn_translation  : String,
        val cn_tags         : String? = null,
        val cn_related      : String? = null,
        val cn_hskLevel     : Int = 0
    )

    @Deprecated("Import via CnVocabularyParser instead. (CSV parsing)")
    fun buildVocabTable() {
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
    }
}