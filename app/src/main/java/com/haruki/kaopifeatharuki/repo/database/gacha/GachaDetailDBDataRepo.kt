package com.haruki.kaopifeatharuki.repo.database.gacha

interface GachaDetailDBDataRepo {
    suspend fun insert(gachaDetailDBData: GachaDetailDBData)

    suspend fun update(gachaDetailDBData: GachaDetailDBData)

    suspend fun delete(gachaDetailDBData: GachaDetailDBData)
}