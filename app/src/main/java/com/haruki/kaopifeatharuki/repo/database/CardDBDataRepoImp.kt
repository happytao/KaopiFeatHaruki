package com.haruki.kaopifeatharuki.repo.database

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBData
import com.haruki.kaopifeatharuki.repo.database.skill.CardSkillDBDataDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class CardDBDataRepoImp(private val cardDBDataDao: CardDBDataDao,
    private val cardSkillDBDataDao: CardSkillDBDataDao): CardDBDataRepo, CardSkillDBDataDao  {

    companion object {
        private const val TAG = "CardDBDataRepoImp"
    }

    override fun getCardDBDataByRarity(rarity: String, pageSize: Int, pageIndex: Int): Flow<List<CardData>> =
        cardDBDataDao.getCardDBDataByRarity(rarity,pageSize, pageIndex).map { cardDBDataList ->
            cardDBDataList.map { cardDBData ->
                CardData(cardDBData)
            }
        }

    override fun getAllCardDBData(pageSize: Int, pageIndex: Int): Flow<List<CardData>> =
        cardDBDataDao.getAllCardDBData(pageSize,pageIndex).map { cardDBDataList ->
            cardDBDataList.map { cardDBData ->
                CardData(cardDBData)
            }
        }

    override fun getCardDBDataById(id: Int): Flow<CardData> =
        cardDBDataDao.getCardDBDataById(id).map {
            CardData(it)
        }

    override suspend fun getCardDBDataByAllParam(
        characterIds: List<Int>,
        attrs: List<String>,
        rarities: List<String>,
        skillTypes: List<String>,
        sortProperty: String,
        isDescSort: Boolean,
        pageSize: Int,
        pageIndex: Int
    ): Flow<List<CardData>> {
        val cardWithSkillFlow = cardDBDataDao.getCardDBDataByAllParam(getQuerySQL(
            characterIds,
            attrs,
            rarities,
            skillTypes,
            sortProperty,
            isDescSort,
            pageSize,
            pageIndex
        ))
        return cardWithSkillFlow.map { cardWithSkillList ->
            cardWithSkillList.map { cardWithSkill ->
                CardData(
                    cardDBData = cardWithSkill.card,
                    skillType = cardWithSkill.skill.skillType
                )
            }
        }


    }

    override suspend fun insert(cardDBData: CardDBData) =
        cardDBDataDao.insert(cardDBData)

    override suspend fun update(cardDBData: CardDBData) =
        cardDBDataDao.update(cardDBData)

    override suspend fun delete(cardDBData: CardDBData) =
        cardDBDataDao.delete(cardDBData)

    private fun getQuerySQL(
        characterIds: List<Int>,
        attrs: List<String>,
        rarities: List<String>,
        skillTypes: List<String>,
        sortProperty: String,
        isDescSort: Boolean,
        pageSize: Int,
        pageIndex: Int
    ): SupportSQLiteQuery {
        val orderClause = if (sortProperty == "cardRarityType") {
            """
        CASE CardDBData.cardRarityType
            WHEN 'rarity_1' THEN 1
            WHEN 'rarity_2' THEN 2
            WHEN 'rarity_3' THEN 3
            WHEN 'rarity_birthday' THEN 4
            WHEN 'rarity_4' THEN 5
            ELSE 6
        END
        """.trimIndent()
        } else {
            "CardDBData.$sortProperty"  // 其他字段直接按列名排序
        }
        val query = """
        SELECT 
        /* CardDBData 字段（前缀 card_） */
        CardDBData.id AS card_id,
        CardDBData.seq AS card_seq,
        CardDBData.characterId AS card_characterId,
        CardDBData.cardRarityType AS card_cardRarityType,
        CardDBData.attr AS card_attr,
        CardDBData.prefix AS card_prefix,
        CardDBData.gachaPhrase AS card_gachaPhrase,
        CardDBData.cardSkillName AS card_cardSkillName,
        CardDBData.releaseAt AS card_releaseAt,
        CardDBData.skillId AS card_skillId,
        CardDBData.assetbundleName AS card_assetbundleName,

        /* CardSkillDBData 字段（前缀 skill_） */
        CardSkillDBData.id AS skill_id,
        CardSkillDBData.description AS skill_description,
        CardSkillDBData.skillFilterId AS skill_skillFilterId,
        CardSkillDBData.descriptionSpriteName AS skill_descriptionSpriteName,
        CardSkillDBData.skillEffects AS skill_skillEffects,
        CardSkillDBData.skillType AS skill_skillType
        FROM CardDBData 
        INNER JOIN CardSkillDBData ON CardDBData.skillId = CardSkillDBData.id
        WHERE CardDBData.characterId IN (${characterIds.joinToString(",")}) 
        AND CardDBData.attr IN (${attrs.joinToString(",") { "'$it'" }}) 
        AND CardDBData.cardRarityType IN (${rarities.joinToString(",") { "'$it'" }})
        AND CardSkillDBData.skillType IN (${skillTypes.joinToString(",") { "'$it'" }})
        ORDER BY $orderClause ${if (isDescSort) "DESC" else "ASC"}
        LIMIT $pageSize OFFSET ${pageIndex * pageSize}
    """.trimIndent()
        Log.i(TAG,"query: $query")
        return SimpleSQLiteQuery(query)
    }



    override suspend fun insert(cardSkillDBData: CardSkillDBData) =
        cardSkillDBDataDao.insert(cardSkillDBData)

    override suspend fun update(cardSkillDBData: CardSkillDBData) =
        cardSkillDBDataDao.update(cardSkillDBData)

    override suspend fun delete(cardSkillDBData: CardSkillDBData) =
        cardSkillDBDataDao.delete(cardSkillDBData)

}