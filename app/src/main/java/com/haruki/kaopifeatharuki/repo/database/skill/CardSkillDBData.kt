package com.haruki.kaopifeatharuki.repo.database.skill

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CardSkillDBData")
data class CardSkillDBData(
    @PrimaryKey
    val id: Int = 0,
    val description: String = "",
    val skillFilterId: Int = 0,
    val descriptionSpriteName: String = "",
    val skillEffects: String = "",
    val skillType: String = ""

)