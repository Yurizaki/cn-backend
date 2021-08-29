package com.holmes.cnbackend.vocabulary

import com.holmes.cnbackend.database.CnCsvParser
import com.holmes.cnbackend.database.CnData

class CnVocabularyParser: CnCsvParser() {
    override fun parseStrings(): MutableList<CnData> {
        val myStrings: MutableList<String> = super.parseCsv("src/main/resources/data/vocab_data.csv")
        val myVocabData: MutableList<CnData> = mutableListOf()

        myStrings.forEach {
            val strSpl = it.split(",")

            myVocabData.add(
                CnVocabData(
                    hanzi = strSpl[0].trim(),
                    pinyin = strSpl[1].trim(),
                    english = strSpl[2].trim(),
                    lesson = strSpl[6].trim(),
                    lessonLevel = strSpl[7].trim().toIntOrNull()
                )
            )
        }

        return myVocabData
    }
}
