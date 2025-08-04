package com.haruki.kaopifeatharuki.repo.database.gacha

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow


@Dao
interface GachaDBDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gachaDBData: GachaDBData)

    @Update
    suspend fun update(gachaDBData: GachaDBData)

    @Delete
    suspend fun delete(gachaDBData: GachaDBData)

    @Query("""
        SELECT 
            detail.*,
            gacha.*
        FROM
            GachaDetailDBData AS detail
            INNER JOIN GachaDBData AS gacha
                ON detail.gachaId = gacha.id 
        WHERE
            detail.cardId = :cardId
        ORDER BY 
            detail.detailId ASC
        LIMIT 1
    """)
    fun getGachaDBDataByCardId(cardId: Int): Flow<GachaWithDetailDBData?>
}