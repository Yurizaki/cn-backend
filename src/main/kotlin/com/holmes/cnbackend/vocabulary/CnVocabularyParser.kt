package com.holmes.cnbackend.vocabulary

import com.holmes.cnbackend.database.CnCsvParser
import com.holmes.cnbackend.database.CnData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CnVocabularyParser: CnCsvParser() {
    override fun parseStrings(): MutableList<CnData> {
        val myStrings: MutableList<String> = super.parseCsv("src/main/resources/data/vocab_data.csv")
        val myVocabData: MutableList<CnData> = mutableListOf()

        myStrings.forEach {
            val strSpl = it.split(",")

            myVocabData.add(
                CnVocabData(
                    id = strSpl[0].trim().toInt(),
                    hanzi = strSpl[1].trim(),
                    pinyin = strSpl[2].trim(),
                    english = strSpl[3].trim(),
                    lesson = strSpl[7].trim(),
                    lessonLevel = strSpl[8].trim().toIntOrNull(),
                    _created = strSpl[9].trim(),
                    _updated = strSpl[10].trim()
                )
            )
        }

        return myVocabData
    }
}
