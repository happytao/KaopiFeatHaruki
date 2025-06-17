package com.haruki.kaopifeatharuki.repo.parser

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.repo.data.skill.CardSkillData
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
        val cardSkillData = GsonUtil.fromJson(reader, CardSkillData::class.java)
        val skillEffectList = cardSkillData!!.skillEffects
        val skillEffectsJson = GsonUtil.toJson(skillEffectList)
        val skillType = if(cardSkillData.descriptionSpriteName == "life_recovery") {
            SKILL_TYPE_HP_BONUS
        } else if(cardSkillData.descriptionSpriteName == "judgment_up") {
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
            id = cardSkillData.id,
            description = cardSkillData.description,
            skillFilterId = cardSkillData.skillFilterId,
            descriptionSpriteName = cardSkillData.descriptionSpriteName,
            skillEffects = skillEffectsJson,
            skillType = skillType

        )
    }



    override suspend fun insertBatch(parseData: List<CardSkillDBData>) {
        parseData.forEach {
            dataRepo.insert(it)
        }
    }
}