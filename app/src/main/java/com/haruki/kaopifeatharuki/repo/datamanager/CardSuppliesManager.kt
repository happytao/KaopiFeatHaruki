package com.haruki.kaopifeatharuki.repo.datamanager

import android.util.Log
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.repo.data.card.CardSupply
import com.haruki.kaopifeatharuki.util.GsonUtil

object CardSuppliesManager {
    private const val TAG = "CardSuppliesManager"

    val cardSuppliesMap:Map<Int, CardSupply> by lazy {
        loadCardSupplies()
    }

    private fun loadCardSupplies():Map<Int, CardSupply> {
        try {
            val cardSuppliesJson = BaseApplication.appContext.resources.assets.open("immutable/cardSupplies.json")
                .bufferedReader().use { it.readText() }
            val list = GsonUtil.fromJson(cardSuppliesJson, Array<CardSupply>::class.java) ?: return mapOf()
            return list.associateBy { it.id }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            return mapOf()
        }
    }


    fun getCardSupplyType(cardSupplyId:Int): String {
        return cardSuppliesMap[cardSupplyId]?.cardSupplyType?:""
    }

}