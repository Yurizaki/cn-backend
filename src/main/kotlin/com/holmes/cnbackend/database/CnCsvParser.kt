package com.holmes.cnbackend.database

import java.io.File

abstract class CnCsvParser {
    fun parseCsv(filePath: String): MutableList<String> {
        val file: File = File(filePath)
        val myStrings: MutableList<String> = mutableListOf()
        var iterator = 0

        file.forEachLine {
            if(iterator != 0) {
                if (it.isNotEmpty() && it.isNotBlank()) {
                    myStrings.add(it)
                }
            }
            iterator++
        }

        return myStrings
    }

    abstract fun parseStrings(): MutableList<CnData>
}