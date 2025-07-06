package com.haruki.kaopifeatharuki.repo.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CardDBData")
data class CardDBData(
    @PrimaryKey
    var id: Int,
    var seq: Int,
    var characterId: Int,
    var cardRarityType: String,
    var attr: String,
    var prefix: String,
    var gachaPhrase: String,
    var cardSkillName: String,
    var releaseAt: Long,
    var skillId: Int,
    var assetbundleName: String,
    var specialTrainingPower1BonusFixed: Int,
    var specialTrainingPower2BonusFixed: Int,
    var specialTrainingPower3BonusFixed: Int,
    var cardParameters: String,
    var specialTrainingCosts: String,
    var basePower: Int,
    var supportUnit: String,
    var cardSupplyId: Int,
    var specialTrainingSkillId: Int?,
    var specialTrainingSkillName: String?,

)


