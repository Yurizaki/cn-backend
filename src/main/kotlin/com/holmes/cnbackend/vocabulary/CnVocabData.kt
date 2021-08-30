package com.holmes.cnbackend.vocabulary;

import com.holmes.cnbackend.database.CnData

data class CnVocabData(
    val id: Int,
    val hanzi: String,
    val pinyin: String,
    val english: String,
    val tags: String? = null,
    val related: String? = null,
    val hskLevel: Int? = null,
    val lesson: String? = null,
    val lessonLevel: Int? = null
): CnData, Comparable<CnVocabData> {

    override fun compareTo(other: CnVocabData): Int {
        return if(other.id == this.id && other.hanzi == this.hanzi) {
            1
        } else {
            0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is CnVocabData) {
            return other.id == this.id && other.hanzi == this.hanzi
        }
        return super.equals(other)
    }


}
