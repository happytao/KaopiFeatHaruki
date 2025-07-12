package com.haruki.kaopifeatharuki.repo.datamanager

import android.util.Log
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.repo.data.card.CardEpisode
import com.haruki.kaopifeatharuki.repo.data.card.CardSupply
import com.haruki.kaopifeatharuki.util.GsonUtil

object CardEpisodesManager {
    private const val TAG = "CardEpisodesManager"

    val cardEpisodesMap:Map<Int, List<CardEpisode>> by lazy {
        loadCardEpisodes()
    }


    private fun loadCardEpisodes():Map<Int, List<CardEpisode>> {
        try {
            val cardEpisodesJson = BaseApplication.appContext.resources.assets.open("cardEpisodes.json")
                .bufferedReader().use { it.readText() }
            val list = GsonUtil.fromJson(cardEpisodesJson, Array<CardEpisode>::class.java) ?: return mapOf()
            return list.groupBy { it.cardId }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            return mapOf()
        }
    }

    fun getFirstPartEpisodesBonus(cardId:Int):Int {
        var bonus = 0
        cardEpisodesMap[cardId]?.forEach {
            if(it.cardEpisodePartType == "first_part") {
                bonus =  it.power1BonusFixed + it.power2BonusFixed + it.power3BonusFixed
            }
        }
        return bonus
    }

    fun getSecondPartEpisodesBonus(cardId:Int):Int {
        var bonus = 0
        cardEpisodesMap[cardId]?.forEach {
            if(it.cardEpisodePartType == "second_part") {
                bonus =  it.power1BonusFixed + it.power2BonusFixed + it.power3BonusFixed
            }
        }
        return bonus
    }
}