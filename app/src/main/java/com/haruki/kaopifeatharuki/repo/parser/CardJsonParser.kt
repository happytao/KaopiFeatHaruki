package com.haruki.kaopifeatharuki.repo.parser

import android.content.Context
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.repo.database.CardDBData
import com.haruki.kaopifeatharuki.repo.database.CardDBDataRepoImp
import com.haruki.kaopifeatharuki.repo.database.CardDataBase

class CardJsonParser(private val context: Context): BaseJsonParser<CardDBData, CardDBDataRepoImp>(context) {
    override val dataRepo: CardDBDataRepoImp by lazy {
        CardDBDataRepoImp(CardDataBase.getDatabase(context).cardDBDataDao(),
            CardDataBase.getDatabase(context).cardSkillDBDataDao())
    }


    override fun parseData(reader: JsonReader): CardDBData {
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

        return CardDBData(
            id = id,
            seq = seq,
            characterId = characterId,
            cardRarityType = cardRarityType,
            attr = attr,
            prefix = prefix,
            gachaPhrase = gachaPhrase,
            cardSkillName = cardSkillName,
            releaseAt = releaseAt,
            skillId = skillId,
            assetbundleName = assetbundleName
        )
    }


    override suspend fun insertBatch(parseData: List<CardDBData>) {
        parseData.forEach {
            dataRepo.insert(it)
        }

    }
}