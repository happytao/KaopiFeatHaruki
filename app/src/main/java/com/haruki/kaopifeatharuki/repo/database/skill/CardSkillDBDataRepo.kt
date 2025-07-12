package com.haruki.kaopifeatharuki.repo.database.skill

import com.haruki.kaopifeatharuki.repo.data.skill.CardSkillData
import kotlinx.coroutines.flow.Flow

interface CardSkillDBDataRepo {
    suspend fun insert(cardSkillDBData: CardSkillDBData)
    suspend fun update(cardSkillDBData: CardSkillDBData)
    suspend fun delete(cardSkillDBData: CardSkillDBData)
    fun getCardSkillDBDataById(skillId: Int): Flow<CardSkillData?>
}