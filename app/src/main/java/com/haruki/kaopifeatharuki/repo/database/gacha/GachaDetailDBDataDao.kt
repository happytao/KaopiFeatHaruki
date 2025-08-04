package com.haruki.kaopifeatharuki.repo.database.gacha

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface GachaDetailDBDataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gachaDetailDBData: GachaDetailDBData)

    @Update
    suspend fun update(gachaDetailDBData: GachaDetailDBData)

    @Delete
    suspend fun delete(gachaDetailDBData: GachaDetailDBData)
}