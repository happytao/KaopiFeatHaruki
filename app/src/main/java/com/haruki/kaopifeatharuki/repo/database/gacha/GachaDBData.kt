package com.haruki.kaopifeatharuki.repo.database.gacha

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haruki.kaopifeatharuki.repo.data.gacha.GachaPickupsItem

@Entity("GachaDBData")
data class GachaDBData(
                     val gachaType: String = "",
                     val gachaInformation: String?,
                     val endAt: Long = 0,
                     val wishFixedSelectCount: Int = 0,
                     val gachaCardRarityRates: String?,
                     val isShowPeriod: Boolean = false,
                     val dailySpinLimit: Int = 0,
                     val wishLimitedSelectCount: Int = 0,
                     val assetbundleName: String = "",
                     val gachaBehaviors: String?,
                     val name: String = "",
                     val gachaDetails: String?,
                     @PrimaryKey
                     val id: Int = 0,
                     val gachaCeilItemId: Int = 0,
                     val wishSelectCount: Int = 0,
                     val seq: Int = 0,
                     val startAt: Long = 0,
                     val gachaPickups: String?
)
