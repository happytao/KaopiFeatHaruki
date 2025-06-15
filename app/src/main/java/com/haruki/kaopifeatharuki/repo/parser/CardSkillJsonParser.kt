package com.haruki.kaopifeatharuki.repo.parser

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.repo.data.skill.SkillEffectsItem
import com.haruki.kaopifeatharuki.repo.database.CardDBData
import com.haruki.kaopifeatharuki.repo.database.CardDBDataRepoImp
import com.haruki.kaopifeatharuki.repo.database.CardDataBase
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBData
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_CHECK_BONUS
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_HP_BONUS
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS_WHEN_BAND
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS_WHEN_HIGH_HP
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS_WHEN_PERFECT
import com.haruki.kaopifeatharuki.util.GsonUtil

class CardSkillJsonParser(private val context: Context): BaseJsonParser<CardSkillDBData, CardDBDataRepoImp>(context) {
    companion object {
        private const val TAG = "CardSkillJsonParser"
    }

    override val dataRepo: CardDBDataRepoImp by lazy {
        CardDBDataRepoImp(CardDataBase.getDatabase(context).cardDBDataDao(),
            CardDataBase.getDatabase(context).cardSkillDBDataDao())
    }

    override fun parseData(reader: JsonReader): CardSkillDBData {
        var id: Int = 0
        var description: String = ""
        var skillFilterId: Int = 0
        var descriptionSpriteName: String = ""
        var skillEffects: String = ""
        var skillType: String = ""


        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> id = reader.nextInt()
                "description" -> description = reader.nextString()
                "skillFilterId" -> skillFilterId = reader.nextInt()
                "descriptionSpriteName" -> descriptionSpriteName = reader.nextString()
                "skillEffects" -> {
                    val jsonArray = JsonParser.parseReader(reader) as JsonArray
                    skillEffects = jsonArray.toString()
                }
                else -> reader.skipValue()
            }

        }
        reader.endObject()

        val skillEffectList = GsonUtil.fromJsonList(skillEffects, SkillEffectsItem::class.java)
        skillType = if(descriptionSpriteName == "life_recovery") {
            SKILL_TYPE_HP_BONUS
        } else if(descriptionSpriteName == "judgment_up") {
            SKILL_TYPE_CHECK_BONUS
        } else {
            if(skillEffectList?.first()?.skillEffectType == "score_up_condition_life") {
                SKILL_TYPE_POINT_BONUS_WHEN_HIGH_HP
            } else if(skillEffectList?.first()?.activateNotesJudgmentType == "perfect") {
                SKILL_TYPE_POINT_BONUS_WHEN_PERFECT
            } else if(skillEffectList?.first()?.skillEnhance != null) {
                //团分卡
                SKILL_TYPE_POINT_BONUS_WHEN_BAND
            } else {
                SKILL_TYPE_POINT_BONUS
            }
        }
        return CardSkillDBData(
            id = id,
            description = description,
            skillFilterId = skillFilterId,
            descriptionSpriteName = descriptionSpriteName,
            skillEffects = skillEffects,
            skillType = skillType

        )
    }



    override suspend fun insertBatch(parseData: List<CardSkillDBData>) {
        parseData.forEach {
            dataRepo.insert(it)
        }
    }
}