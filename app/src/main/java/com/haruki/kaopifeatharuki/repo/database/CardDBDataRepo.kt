package com.haruki.kaopifeatharuki.repo.database

import kotlinx.coroutines.flow.Flow

interface CardDBDataRepo {

    fun getCardDBDataByRarity(rarity: String,pageSize: Int, pageIndex: Int): Flow<List<CardDBData>>

    fun getCardDBDataById(id: Int): Flow<CardDBData>

    suspend fun insert(cardDBData: CardDBData)

    suspend fun update(cardDBData: CardDBData)

    suspend fun delete(cardDBData: CardDBData)

}