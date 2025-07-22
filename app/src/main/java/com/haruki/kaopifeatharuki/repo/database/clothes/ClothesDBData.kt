package com.haruki.kaopifeatharuki.repo.database.clothes

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "ClothesDBData")
data class ClothesDBData(
                         var howToObtain: String?,
                         var colorName: String,
                         var publishedAt: Long,
                         var archivePublishedAt: Long = 0,
                         var colorId: Int,
                         var costumeDType: String,
                         var designer: String,
                         var costumeDGroupId: Int,
                         var assetbundleName: String,
                         var costumeDRarity: String,
                         var name: String,
                         var archiveDisplayType: String,
                         @PrimaryKey
                         var id: Int,
                         var characterId: Int,
                         var partType: String,
                         var seq: Int,
                         var cardId:Int?)
