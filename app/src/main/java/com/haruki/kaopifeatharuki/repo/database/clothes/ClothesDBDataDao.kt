package com.haruki.kaopifeatharuki.repo.database.clothes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothesDBDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(clothesDBData: ClothesDBData)

    @Update
    suspend fun update(clothesDBData: ClothesDBData)

    @Delete
    suspend fun delete(clothesDBData: ClothesDBData)

    @Query("SELECT * FROM ClothesDBData WHERE cardId = :cardId")
    fun getClothesDBDataByCardId(cardId: Int): Flow<List<ClothesDBData>>

}