package com.haruki.kaopifeatharuki.repo.database.gacha

import com.haruki.kaopifeatharuki.repo.data.gacha.GachaData
import kotlinx.coroutines.flow.Flow


interface GachaDBDataRepo {
    suspend fun insert(gachaDBData: GachaDBData)

    suspend fun update(gachaDBData: GachaDBData)

    suspend fun delete(gachaDBData: GachaDBData)

    fun getGachaDataByCardId(cardId: Int): Flow<GachaData?>
}