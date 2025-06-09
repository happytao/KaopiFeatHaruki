package com.haruki.kaopifeatharuki.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CardDBData::class], version = 1, exportSchema = false)
abstract class CardDataBase: RoomDatabase() {

    abstract fun cardDBDataDao(): CardDBDataDao


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