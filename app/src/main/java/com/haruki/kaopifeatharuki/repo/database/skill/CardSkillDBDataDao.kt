package com.haruki.kaopifeatharuki.repo.database.skill

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haruki.kaopifeatharuki.repo.database.CardDBData
import kotlinx.coroutines.flow.Flow

@Dao
interface CardSkillDBDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardSkillDBData: CardSkillDBData)

    @Update
    suspend fun update(cardSkillDBData: CardSkillDBData)

    @Delete
    suspend fun delete(cardSkillDBData: CardSkillDBData)

    @Query("SELECT * FROM CardSkillDBData WHERE id = :skillId")
    fun getCardSkillDBDataById(skillId: Int): Flow<CardSkillDBData?>


}