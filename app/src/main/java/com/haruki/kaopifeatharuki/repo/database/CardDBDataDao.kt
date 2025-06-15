package com.haruki.kaopifeatharuki.repo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBData
import com.haruki.kaopifeatharuki.repo.database.skill.CardWithSkillDBData
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

    @Query("""
        SELECT * FROM CardDBData 
        WHERE cardRarityType = :rarity 
        ORDER BY releaseAt DESC
        LIMIT :pageSize OFFSET (:pageIndex * :pageSize)
    """)
    fun getCardDBDataByRarity(rarity: String, pageSize: Int, pageIndex: Int): Flow<List<CardDBData>>

    @Query("""
        SELECT * FROM CardDBData
        ORDER BY releaseAt DESC
        LIMIT :pageSize OFFSET (:pageIndex * :pageSize)
    """)
    fun getAllCardDBData(pageSize: Int, pageIndex: Int): Flow<List<CardDBData>>


    @RawQuery(observedEntities = [CardDBData::class,CardSkillDBData::class])
    fun getCardDBDataByAllParam(query: SupportSQLiteQuery): Flow<List<CardWithSkillDBData>>
}

