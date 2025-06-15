package com.haruki.kaopifeatharuki.repo.parser

import android.content.Context
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.repo.database.CardDBDataRepoImp
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

abstract class BaseJsonParser<T,V>(private val context: Context) {

    companion object {
        private const val TAG = "BaseJsonParser"
        private const val BATCH_SIZE = 200
    }

    protected abstract val dataRepo: V

    suspend fun importJson(inputStream: InputStream) {
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            JsonReader(reader).use { jsonReader ->
                jsonReader.beginArray()

                val batch = mutableListOf<T>()

                while(jsonReader.hasNext()) {
                    val cardData = parseData(jsonReader)
                    batch.add(cardData)

                    if(batch.size >= BATCH_SIZE) {
                        insertBatch(batch)
                        batch.clear()
                    }
                }

                if(batch.isNotEmpty()) {
                    insertBatch(batch)
                }

                jsonReader.endArray()
            }
        }
    }

    abstract fun parseData(reader: JsonReader) : T

    abstract suspend fun insertBatch(parseData:List<T>)

}