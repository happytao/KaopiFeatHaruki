package com.haruki.kaopifeatharuki.repo.parser

import android.content.Context
import com.google.gson.stream.JsonReader
import com.haruki.kaopifeatharuki.repo.data.gacha.GachaData
import com.haruki.kaopifeatharuki.repo.database.GameDataBase
import com.haruki.kaopifeatharuki.repo.database.gacha.GachaDBData
import com.haruki.kaopifeatharuki.repo.database.gacha.GachaDBDataRepoImp
import com.haruki.kaopifeatharuki.repo.database.gacha.GachaDetailDBData
import com.haruki.kaopifeatharuki.util.GsonUtil

class GachaJsonParser(private val context: Context)
    : BaseJsonParser<Pair<GachaDBData, List<GachaDetailDBData>>, GachaDBDataRepoImp>(context) {
    override val dataRepo: GachaDBDataRepoImp by lazy {
        GachaDBDataRepoImp(GameDataBase.getDatabase(context).gachaDBDataDao(),
            GameDataBase.getDatabase(context).gachaDetailDBDataDao())
    }

    override fun parseData(reader: JsonReader): Pair<GachaDBData, List<GachaDetailDBData>> {
        val gachaData = GsonUtil.fromJson(reader, GachaData::class.java)
        val gachaInformationJson = GsonUtil.toJson(gachaData?.gachaInformation)
        val gachaCardRarityRatesJson = GsonUtil.toJson(gachaData?.gachaCardRarityRates)
        val gachaBehaviorsJson = GsonUtil.toJson(gachaData?.gachaBehaviors)
        val gachaDetailsJson = GsonUtil.toJson(gachaData?.gachaDetails)
        val gachaPickupsJson = GsonUtil.toJson(gachaData?.gachaPickups)

        val gachaDBData = GachaDBData(
            gachaType = gachaData!!.gachaType,
            gachaInformation = gachaInformationJson,
            endAt = gachaData.endAt,
            wishFixedSelectCount = gachaData.wishFixedSelectCount,
            gachaCardRarityRates = gachaCardRarityRatesJson,
            isShowPeriod = gachaData.isShowPeriod,
            dailySpinLimit = gachaData.dailySpinLimit,
            wishLimitedSelectCount = gachaData.wishLimitedSelectCount,
            assetbundleName = gachaData.assetbundleName,
            gachaBehaviors = gachaBehaviorsJson,
            name = gachaData.name,
            gachaDetails = gachaDetailsJson,
            id = gachaData.id,
            gachaCeilItemId = gachaData.gachaCeilItemId,
            wishSelectCount = gachaData.wishSelectCount,
            seq = gachaData.seq,
            startAt = gachaData.startAt,
            gachaPickups = gachaPickupsJson
        )

        val gachaDetailList = gachaData.gachaDetails?.map {
            GachaDetailDBData(
                gachaId = it.gachaId,
                cardId = it.cardId,
                weight = it.weight,
                id = it.id,
                isWish = it.isWish
            )
        }?:mutableListOf()

        return Pair(gachaDBData, gachaDetailList)

    }

    override suspend fun insertBatch(parseData: List<Pair<GachaDBData, List<GachaDetailDBData>>>) {
        parseData.forEach { pair ->
            dataRepo.insert(pair.first)
            pair.second.forEach { detail ->
                dataRepo.insert(detail)
            }
        }
    }
}