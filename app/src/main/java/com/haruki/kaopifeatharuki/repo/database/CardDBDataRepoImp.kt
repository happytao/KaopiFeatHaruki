package com.haruki.kaopifeatharuki.repo.database

import kotlinx.coroutines.flow.Flow

class CardDBDataRepoImp(private val cardDBDataDao: CardDBDataDao): CardDBDataRepo  {

    override fun getCardDBDataByRarity(rarity: String, pageSize: Int, pageIndex: Int): Flow<List<CardDBData>> =
        cardDBDataDao.getCardDBDataByRarity(rarity,pageSize, pageIndex)

    override fun getCardDBDataById(id: Int): Flow<CardDBData> =
        cardDBDataDao.getCardDBDataById(id)

    override suspend fun insert(cardDBData: CardDBData) =
        cardDBDataDao.insert(cardDBData)

    override suspend fun update(cardDBData: CardDBData) =
        cardDBDataDao.update(cardDBData)

    override suspend fun delete(cardDBData: CardDBData) =
        cardDBDataDao.delete(cardDBData)

}