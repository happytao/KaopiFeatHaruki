package com.haruki.kaopifeatharuki.repo.database.gacha

import androidx.sqlite.db.SupportSQLiteQuery
import com.haruki.kaopifeatharuki.repo.data.gacha.GachaData
import com.haruki.kaopifeatharuki.repo.data.gacha.GachaDetailsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GachaDBDataRepoImp(
    private val gachaDao: GachaDBDataDao,
    private val gachaDetailDao: GachaDetailDBDataDao
): GachaDBDataRepo, GachaDetailDBDataRepo {
    override suspend fun insert(gachaDBData: GachaDBData) =
        gachaDao.insert(gachaDBData)

    override suspend fun update(gachaDBData: GachaDBData) =
        gachaDao.update(gachaDBData)

    override suspend fun delete(gachaDBData: GachaDBData) =
        gachaDao.delete(gachaDBData)

    override fun getGachaDataByCardId(cardId: Int): Flow<GachaData?> {
        return gachaDao.getGachaDBDataByCardId(cardId).map {
            it ?: return@map null
            GachaData(
                gachaType = it.gacha.gachaType,
                gachaInformation = null,
                endAt = it.gacha.endAt,
                wishFixedSelectCount = it.gacha.wishFixedSelectCount,
                gachaCardRarityRates = null,
                isShowPeriod = it.gacha.isShowPeriod,
                dailySpinLimit = it.gacha.dailySpinLimit,
                wishLimitedSelectCount = it.gacha.wishLimitedSelectCount,
                assetbundleName = it.gacha.assetbundleName,
                gachaBehaviors = null,
                name = it.gacha.name,
                gachaDetails = listOf(GachaDetailsItem(
                    gachaId = it.gachaDetail.gachaId,
                    cardId = it.gachaDetail.cardId,
                    weight = it.gachaDetail.weight,
                    id = it.gachaDetail.id,
                    isWish = it.gachaDetail.isWish
                )),
                id = it.gacha.id,
                gachaCeilItemId = it.gacha.gachaCeilItemId,
                wishSelectCount = it.gacha.wishSelectCount,
                seq = it.gacha.seq,
                startAt = it.gacha.startAt,
                gachaPickups = null
            )
        }
    }

    override suspend fun insert(gachaDetailDBData: GachaDetailDBData) =
        gachaDetailDao.insert(gachaDetailDBData)

    override suspend fun update(gachaDetailDBData: GachaDetailDBData) =
        gachaDetailDao.update(gachaDetailDBData)

    override suspend fun delete(gachaDetailDBData: GachaDetailDBData) =
        gachaDetailDao.delete(gachaDetailDBData)


}