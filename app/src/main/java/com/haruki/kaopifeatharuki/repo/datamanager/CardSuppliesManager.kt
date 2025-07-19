package com.haruki.kaopifeatharuki.repo.datamanager

import android.util.Log
import android.util.SparseArray
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.repo.data.card.CardSupply
import com.haruki.kaopifeatharuki.util.GsonUtil

object CardSuppliesManager {
    private const val TAG = "CardSuppliesManager"

    private val cardSuppliesMap: Map<Int, CardSupply> by lazy {
        loadCardSupplies()
    }

    private val supplyTypeCache: SparseArray<String> by lazy {
        createSupplyTypeCache()
    }

    private fun loadCardSupplies(): Map<Int, CardSupply> {
        return try {
            val cardSuppliesJson = BaseApplication.appContext.resources.assets
                .open("immutable/cardSupplies.json")
                .bufferedReader()
                .use { it.readText() }

            GsonUtil.fromJson(cardSuppliesJson, Array<CardSupply>::class.java)
                ?.associateBy { it.id }
                ?: emptyMap()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load card supplies data", e)
            emptyMap()
        }
    }

    private fun createSupplyTypeCache(): SparseArray<String> {
        val cache = SparseArray<String>(cardSuppliesMap.size)
        cardSuppliesMap.forEach { (id, supply) ->
            cache.put(id, supply.cardSupplyType ?: "")
        }
        return cache
    }

    fun getCardSupplyType(cardSupplyId: Int): String {
        return supplyTypeCache.get(cardSupplyId, "")
    }

}