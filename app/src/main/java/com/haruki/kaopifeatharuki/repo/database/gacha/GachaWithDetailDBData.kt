package com.haruki.kaopifeatharuki.repo.database.gacha

import androidx.room.Embedded

data class GachaWithDetailDBData(
    @Embedded val gacha: GachaDBData,
    @Embedded val gachaDetail: GachaDetailDBData
)
