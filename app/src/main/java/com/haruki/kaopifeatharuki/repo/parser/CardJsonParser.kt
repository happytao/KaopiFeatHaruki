package com.haruki.kaopifeatharuki.repo.parser

import android.content.Context
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.repo.database.CardDBData
import com.haruki.kaopifeatharuki.repo.database.CardDBDataRepoImp
import com.haruki.kaopifeatharuki.repo.database.CardDataBase
import com.haruki.kaopifeatharuki.util.GsonUtil

class CardJsonParser(private val context: Context): BaseJsonParser<CardDBData, CardDBDataRepoImp>(context) {
    override val dataRepo: CardDBDataRepoImp by lazy {
        CardDBDataRepoImp(CardDataBase.getDatabase(context).cardDBDataDao(),
            CardDataBase.getDatabase(context).cardSkillDBDataDao())
    }


    override fun parseData(reader: JsonReader): CardDBData {
        val cardData = GsonUtil.fromJson(reader, CardData::class.java)
        val basePower = (cardData?.cardParameters?.param1?.lastOrNull() ?: 0) +
                (cardData?.cardParameters?.param2?.lastOrNull() ?: 0) +
                (cardData?.cardParameters?.param3?.lastOrNull() ?: 0)
        val parametersJson = GsonUtil.toJson(cardData?.cardParameters)
        val specialTrainingCostsJson = GsonUtil.toJson(cardData?.specialTrainingCosts)
        return CardDBData(
            id = cardData!!.id,
            seq = cardData.seq,
            characterId = cardData.characterId,
            cardRarityType = cardData.cardRarityType,
            attr = cardData.attr,
            prefix = cardData.prefix,
            gachaPhrase = cardData.gachaPhrase,
            cardSkillName = cardData.cardSkillName,
            releaseAt = cardData.releaseAt,
            skillId = cardData.skillId,
            assetbundleName = cardData.assetbundleName,
            specialTrainingPower1BonusFixed = cardData.specialTrainingPower1BonusFixed,
            specialTrainingPower2BonusFixed = cardData.specialTrainingPower2BonusFixed,
            specialTrainingPower3BonusFixed = cardData.specialTrainingPower3BonusFixed,
            cardParameters = parametersJson,
            specialTrainingCosts = specialTrainingCostsJson,
            basePower = basePower
        )
    }


    override suspend fun insertBatch(parseData: List<CardDBData>) {
        parseData.forEach {
            dataRepo.insert(it)
        }

    }
}