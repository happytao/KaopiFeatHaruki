package com.haruki.kaopifeatharuki.repo.database.clothes

import com.haruki.kaopifeatharuki.repo.data.clothes.ClothesData
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBData
import kotlinx.coroutines.flow.Flow

interface ClothesDBDataRepo {
    suspend fun insert(clothesDBData: ClothesDBData)
    suspend fun update(clothesDBData: ClothesDBData)
    suspend fun delete(clothesDBData: ClothesDBData)
    fun getClothesDBDataByCardId(cardId: Int): Flow<List<ClothesData>>
}