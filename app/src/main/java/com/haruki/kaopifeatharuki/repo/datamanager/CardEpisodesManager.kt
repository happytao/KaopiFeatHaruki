package com.haruki.kaopifeatharuki.repo.datamanager

import android.util.Log
import android.util.SparseArray
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.repo.data.card.CardEpisode
import com.haruki.kaopifeatharuki.util.GsonUtil

object CardEpisodesManager {
    private const val TAG = "CardEpisodesManager"

    // 原始数据映射
    private val cardEpisodesMap: Map<Int, List<CardEpisode>> by lazy {
        loadCardEpisodes()
    }

    // 使用组合缓存存储两个奖励值
    private data class EpisodeBonuses(val firstPart: Int, val secondPart: Int)
    private val bonusCache: SparseArray<EpisodeBonuses> by lazy {
        createBonusCache()
    }

    private fun loadCardEpisodes(): Map<Int, List<CardEpisode>> {
        return try {
            val cardEpisodesJson = BaseApplication.appContext.resources.assets
                .open("cardEpisodes.json")
                .bufferedReader()
                .use { it.readText() }

            GsonUtil.fromJson(cardEpisodesJson, Array<CardEpisode>::class.java)
                ?.groupBy { it.cardId }
                ?: emptyMap()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load card episodes", e)
            emptyMap()
        }
    }

    private fun createBonusCache(): SparseArray<EpisodeBonuses> {
        val cache = SparseArray<EpisodeBonuses>(cardEpisodesMap.size)

        cardEpisodesMap.forEach { (cardId, episodes) ->
            // 直接处理两个固定元素
            val firstPart = episodes.firstOrNull { it.cardEpisodePartType == "first_part" }
            val secondPart = episodes.firstOrNull { it.cardEpisodePartType == "second_part" }

            // 计算奖励值
            val firstBonus = firstPart?.run { power1BonusFixed + power2BonusFixed + power3BonusFixed } ?: 0
            val secondBonus = secondPart?.run { power1BonusFixed + power2BonusFixed + power3BonusFixed } ?: 0

            cache.put(cardId, EpisodeBonuses(firstBonus, secondBonus))
        }

        return cache
    }

    fun getFirstPartEpisodesBonus(cardId: Int): Int {
        return bonusCache.get(cardId)?.firstPart ?: 0
    }

    fun getSecondPartEpisodesBonus(cardId: Int): Int {
        return bonusCache.get(cardId)?.secondPart ?: 0
    }

    fun getBothBonuses(cardId: Int): Pair<Int, Int> {
        return bonusCache.get(cardId)?.let { it.firstPart to it.secondPart } ?: (0 to 0)
    }
}