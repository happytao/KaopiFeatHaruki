package com.haruki.kaopifeatharuki.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haruki.kaopifeatharuki.repo.database.card.CardDBData
import com.haruki.kaopifeatharuki.repo.database.card.CardDBDataDao
import com.haruki.kaopifeatharuki.repo.database.clothes.ClothesDBData
import com.haruki.kaopifeatharuki.repo.database.clothes.ClothesDBDataDao
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBData
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBDataDao

@Database(entities = [
    CardDBData::class,
    CardSkillDBData::class,
    ClothesDBData::class ], version = 1, exportSchema = false)
abstract class GameDataBase: RoomDatabase() {

    abstract fun cardDBDataDao(): CardDBDataDao

    abstract fun cardSkillDBDataDao(): CardSkillDBDataDao

    abstract fun clothesDBDataDao(): ClothesDBDataDao


    companion object {
        @Volatile
        private var instance:GameDataBase? = null


        fun getDatabase(context: Context): GameDataBase {

            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDataBase::class.java, "game_database")
                    .build()
                    .also { instance = it }
            }
        }
    }

}