package com.haruki.kaopifeatharuki.repo.parser

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.repo.data.clothes.ClothesData
import com.haruki.kaopifeatharuki.repo.database.GameDataBase
import com.haruki.kaopifeatharuki.repo.database.clothes.ClothesDBData
import com.haruki.kaopifeatharuki.repo.database.clothes.ClothesDBDataRepoImp
import com.haruki.kaopifeatharuki.util.GsonUtil


class ClothesJsonParser(private val context: Context)
    : BaseJsonParser<ClothesDBData, ClothesDBDataRepoImp>(context) {
    override val dataRepo: ClothesDBDataRepoImp by lazy {
        ClothesDBDataRepoImp(GameDataBase.getDatabase(context).clothesDBDataDao())

    }
    private val cardIdMap:Map<Int, Int> by lazy {
        createCardIdMap()
    }

    override fun parseData(reader: JsonReader): ClothesDBData {
        val clothesData = GsonUtil.fromJson(reader, ClothesData::class.java)
        return ClothesDBData(
            howToObtain = clothesData!!.howToObtain,
            colorName = clothesData.colorName,
            publishedAt = clothesData.publishedAt,
            archivePublishedAt = clothesData.archivePublishedAt,
            colorId = clothesData.colorId,
            costumeDType = clothesData.costumeDType,
            designer = clothesData.designer,
            costumeDGroupId = clothesData.costumeDGroupId,
            assetbundleName = clothesData.assetbundleName,
            costumeDRarity = clothesData.costumeDRarity,
            name = clothesData.name,
            archiveDisplayType = clothesData.archiveDisplayType,
            id = clothesData.id,
            characterId = clothesData.characterId,
            partType = clothesData.partType,
            seq = clothesData.seq,
            cardId = cardIdMap[clothesData.id])
    }

    override suspend fun insertBatch(parseData: List<ClothesDBData>) {
        parseData.forEach {
            dataRepo.insert(it)
        }
    }

    private fun createCardIdMap():Map<Int, Int> {
        val json = BaseApplication.appContext.resources.assets
            .open("cardCostume3ds.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Map<String, Int>>>() {}.type
        val list = GsonUtil.fromJson<List<Map<String, Int>>>(json, type)
        return list?.associate {  it["costume3dId"]!! to it["cardId"]!!  }?: mapOf()
    }
}