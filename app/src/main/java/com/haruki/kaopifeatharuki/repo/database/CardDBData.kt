package com.haruki.kaopifeatharuki.repo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haruki.kaopifeatharuki.repo.data.CardData

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
    var assetbundleName: String


) {

    constructor(cardData: CardData) : this(
            id = cardData.id,
            seq = cardData.seq,
            characterId = cardData.characterId,
            cardRarityType = cardData.cardRarityType,
            attr = cardData.attr,
            prefix = cardData.prefix,
            gachaPhrase = cardData.gachaPhrase,
            cardSkillName = cardData.cardSkillName,
            releaseAt = cardData.releaseAt,
            skillId = cardData.skillId,
            assetbundleName = cardData.assetbundleName)
}


