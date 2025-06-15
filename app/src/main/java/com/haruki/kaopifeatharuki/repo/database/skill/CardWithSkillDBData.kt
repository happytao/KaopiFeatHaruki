package com.haruki.kaopifeatharuki.repo.database.skill

import androidx.room.Embedded
import com.haruki.kaopifeatharuki.repo.database.CardDBData

data class CardWithSkillDBData(
    @Embedded(prefix = "card_") val card:CardDBData,
    @Embedded(prefix = "skill_") val skill: CardSkillDBData
)
