package com.holmes.cnbackend.vocabulary;

import com.holmes.cnbackend.database.CnData

data class CnVocabData(
    val hanzi: String,
    val pinyin: String,
    val english: String,
    val tags: String? = null,
    val related: String? = null,
    val hskLevel: Int? = null,
    val lesson: String? = null,
    val lessonLevel: Int? = null
): CnData {
}
