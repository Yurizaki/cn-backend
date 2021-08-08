package main

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

class CnCharacterInserts {
    fun executeAllCharacterInserts() {
        val charMap: MutableList<cn_Char> = mutableListOf()
        charMap.add(cn_Char("你",   "nǐ"))

        transaction {
            addLogger(StdOutSqlLogger)
            val allChars = cn_Characters.batchInsert(charMap) { vocab ->
                this[cn_Characters.cn_c_character] = vocab.cn_character
                this[cn_Characters.cn_c_pinyin] = vocab.cn_pinyin
            }

            println("Inserts inserted: $allChars")
        }
    }

}