package com.haruki.kaopifeatharuki.repo.database

import com.haruki.kaopifeatharuki.repo.data.CardData
import kotlinx.coroutines.flow.Flow

interface CardDBDataRepo {

    fun getCardDBDataByRarity(rarity: String,pageSize: Int, pageIndex: Int): Flow<List<CardData>>

    fun getAllCardDBData(pageSize: Int, pageIndex: Int): Flow<List<CardData>>

    fun getCardDBDataById(id: Int): Flow<CardData>

    suspend fun getCardDBDataByAllParam(
        characterIds: List<Int>,
        attrs: List<String>,
        rarities: List<String>,
        skillTypes: List<String>,
        sortProperty: String,
        isDescSort: Boolean,
        pageSize: Int,
        pageIndex: Int
    ): Flow<List<CardData>>

    suspend fun insert(cardDBData: CardDBData)

    suspend fun update(cardDBData: CardDBData)

    suspend fun delete(cardDBData: CardDBData)

}