package com.haruki.kaopifeatharuki.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.repo.database.CardDBData
import com.haruki.kaopifeatharuki.repo.database.CardDBDataRepoImp
import com.haruki.kaopifeatharuki.repo.database.CardDataBase
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class CardJsonParser(private val context: Context) {

    companion object {
        private const val TAG = "CardJsonParser"
        private const val BATCH_SIZE = 200
    }

    private val cardRepo:CardDBDataRepoImp by lazy {
        CardDBDataRepoImp(CardDataBase.getDatabase(context).cardDBDataDao())
    }

    suspend fun importJson(inputStream: InputStream) {
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            JsonReader(reader).use { jsonReader ->
                jsonReader.beginArray()

                val batch = mutableListOf<CardData>()

                while(jsonReader.hasNext()) {
                    val cardData = parseCardData(jsonReader)
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

    fun parseCardData(reader: JsonReader): CardData {
        var id: Int = 0
        var specialTrainingPower3BonusFixed: Int = 0
        var prefix: String = ""
        var archivePublishedAt: Long = 0
        var gachaPhrase: String = ""
        var cardSkillName: String = ""
        var releaseAt: Long = 0
        var skillId: Int = 0
        var assetbundleName: String = ""
        var cardRarityType: String = ""
        var archiveDisplayType: String = ""
        var specialTrainingPower2BonusFixed: Int = 0
        var supportUnit: String = ""
        var attr: String = ""
        var characterId: Int = 0
        var specialTrainingPower1BonusFixed: Int = 0
        var seq: Int = 0



        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> id = reader.nextInt()
                "specialTrainingPower3BonusFixed" -> specialTrainingPower3BonusFixed = reader.nextInt()
                "prefix" -> prefix = reader.nextString()
                "archivePublishedAt" -> archivePublishedAt = reader.nextLong()
                "gachaPhrase" -> gachaPhrase = reader.nextString()
                "cardSkillName" -> cardSkillName = reader.nextString()
                "releaseAt" -> releaseAt = reader.nextLong()
                "skillId" -> skillId = reader.nextInt()
                "assetbundleName" -> assetbundleName = reader.nextString()
                "cardRarityType" -> cardRarityType = reader.nextString()
                "archiveDisplayType" -> archiveDisplayType = reader.nextString()
                "specialTrainingPower2BonusFixed" -> specialTrainingPower2BonusFixed = reader.nextInt()
                "supportUnit" -> supportUnit = reader.nextString()
                "attr" -> attr = reader.nextString()
                "characterId" -> characterId = reader.nextInt()
                "specialTrainingPower1BonusFixed" -> specialTrainingPower1BonusFixed = reader.nextInt()
                "seq" -> seq = reader.nextInt()
                else -> reader.skipValue()
            }

        }
        reader.endObject()

        return CardData(id = id,
            specialTrainingPower3BonusFixed = specialTrainingPower3BonusFixed,
            prefix = prefix,
            archivePublishedAt = archivePublishedAt,
            gachaPhrase = gachaPhrase,
            cardSkillName = cardSkillName,
            releaseAt = releaseAt,
            skillId = skillId,
            assetbundleName = assetbundleName,
            cardRarityType = cardRarityType,
            archiveDisplayType = archiveDisplayType,
            specialTrainingPower2BonusFixed = specialTrainingPower2BonusFixed,
            supportUnit = supportUnit,
            attr = attr,
            characterId = characterId,
            specialTrainingPower1BonusFixed = specialTrainingPower1BonusFixed,
            seq = seq).also {
                Log.i(TAG,"parseCardData: $it")
        }

    }

    private suspend fun insertBatch(batch: List<CardData>) {
        batch.forEach {
            cardRepo.insert(CardDBData(it))
        }

    }
}