package com.haruki.kaopifeatharuki.repo.datamanager

import android.util.Log
import android.util.SparseArray
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.repo.data.character.CharacterData
import com.haruki.kaopifeatharuki.util.GsonUtil

object CharacterInfoManager {
    private const val TAG = "CharacterInfoManager"

    private val characterMap: Map<Int, CharacterData> by lazy {
        loadCharacterData()
    }

    private val fullNameCache: SparseArray<String> by lazy {
        createFullNameCache()
    }

    private val unitCache: SparseArray<String> by lazy {
        createUnitCache()
    }

    private fun loadCharacterData(): Map<Int, CharacterData> {
        return try {
            val characterJson = BaseApplication.appContext.resources.assets
                .open("immutable/gameCharacters.json")
                .bufferedReader()
                .use { it.readText() }

            GsonUtil.fromJson(characterJson, Array<CharacterData>::class.java)
                ?.associateBy { it.id }
                ?: emptyMap()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load character data", e)
            emptyMap()
        }
    }

    private fun createFullNameCache(): SparseArray<String> {
        val cache = SparseArray<String>(characterMap.size)
        characterMap.forEach { (id, data) ->
            cache.put(id, "${data.firstName}${data.givenName}")
        }
        return cache
    }

    private fun createUnitCache(): SparseArray<String> {
        val cache = SparseArray<String>(characterMap.size)
        characterMap.forEach { (id, data) ->
            cache.put(id, data.unit ?: "")
        }
        return cache
    }

    fun getCharacterUnit(characterId: Int): String {
        return unitCache.get(characterId, "")
    }

    fun getCharacterName(characterId: Int): String {
        return fullNameCache.get(characterId, "")
    }

}