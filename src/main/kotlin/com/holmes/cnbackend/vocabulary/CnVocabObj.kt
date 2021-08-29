package com.holmes.cnbackend.vocabulary

import com.holmes.cnbackend.config.DB_VOCAB
import org.jetbrains.exposed.sql.Table

object CnVocabObj : Table(DB_VOCAB) {
    val id = integer("id").autoIncrement()
    val hanzi = varchar("hanzi", 10)
    val pinyin = varchar("pinyin", 60)
    val english = varchar("english", 100)
    val tags = varchar("tags", 100).nullable()
    val related = varchar("related", 100).nullable()
    val hskLevel = integer("hskLevel").nullable()
    val lesson = varchar("lesson", 10).nullable()
    val lessonLevel = integer("lessonLevel").nullable()

    override val primaryKey = PrimaryKey(id, name = "CnVocabT_pk")
}