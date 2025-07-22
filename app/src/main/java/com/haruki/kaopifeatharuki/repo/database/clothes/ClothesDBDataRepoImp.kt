package com.haruki.kaopifeatharuki.repo.database.clothes

import com.haruki.kaopifeatharuki.repo.data.clothes.ClothesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClothesDBDataRepoImp(
    private val clothesDBDataDao: ClothesDBDataDao
):ClothesDBDataRepo {
    override suspend fun insert(clothesDBData: ClothesDBData) {
        clothesDBDataDao.insert(clothesDBData)
    }

    override suspend fun update(clothesDBData: ClothesDBData) {
        clothesDBDataDao.update(clothesDBData)
    }

    override suspend fun delete(clothesDBData: ClothesDBData) {
        clothesDBDataDao.delete(clothesDBData)
    }

    override fun getClothesDBDataByCardId(cardId: Int): Flow<List<ClothesData>> {
       return clothesDBDataDao.getClothesDBDataByCardId(cardId)
           .map { clothesDBDataList ->
               clothesDBDataList.map { clothesDBData ->
                   ClothesData(clothesDBData)
               }
           }
    }
}