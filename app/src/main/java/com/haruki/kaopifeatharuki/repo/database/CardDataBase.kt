package com.haruki.kaopifeatharuki.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBData
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBDataDao

@Database(entities = [CardDBData::class, CardSkillDBData::class], version = 1, exportSchema = false)
abstract class CardDataBase: RoomDatabase() {

    abstract fun cardDBDataDao(): CardDBDataDao

    abstract fun cardSkillDBDataDao(): CardSkillDBDataDao


    companion object {
        @Volatile
        private var instance:CardDataBase? = null


        fun getDatabase(context: Context): CardDataBase {

            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, CardDataBase::class.java, "card_database")
                    .build()
                    .also { instance = it }
            }
        }
    }

}