package com.haruki.kaopifeatharuki.repo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDBDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardDBData: CardDBData)

    @Update
    suspend fun update(cardDBData: CardDBData)

    @Delete
    suspend fun delete(cardDBData: CardDBData)

    @Query("SELECT * FROM CardDBData WHERE id = :id")
    fun getCardDBDataById(id: Int): Flow<CardDBData>

    @Query("SELECT * FROM CardDBData WHERE cardRarityType = :rarity ORDER BY releaseAt DESC")
    fun getCardDBDataByRarity(rarity: String): Flow<List<CardDBData>>
}