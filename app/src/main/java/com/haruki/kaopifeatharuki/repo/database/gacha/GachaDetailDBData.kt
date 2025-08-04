package com.haruki.kaopifeatharuki.repo.database.gacha

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("GachaDetailDBData")
data class GachaDetailDBData(
                             val gachaId: Int = 0,
                             val cardId: Int = 0,
                             val weight: Int = 0,
                             @PrimaryKey
                             @ColumnInfo(name = "detailId")
                             val id: Int = 0,
                             val isWish: Boolean = false)
