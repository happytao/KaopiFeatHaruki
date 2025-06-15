package com.haruki.kaopifeatharuki.repo.database.skill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.haruki.kaopifeatharuki.repo.database.CardDBData
@Dao
interface CardSkillDBDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardSkillDBData: CardSkillDBData)

    @Update
    suspend fun update(cardSkillDBData: CardSkillDBData)

    @Delete
    suspend fun delete(cardSkillDBData: CardSkillDBData)


}