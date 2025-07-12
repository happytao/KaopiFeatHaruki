package com.haruki.kaopifeatharuki.repo.datamanager

import android.util.Log
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.repo.data.character.CharacterData
import com.haruki.kaopifeatharuki.util.GsonUtil

object CharacterInfoManager {
    private const val TAG = "CharacterInfoManager"

    val characterMap:Map<Int, CharacterData> by lazy {
        loadCharacterData()
    }

    private fun loadCharacterData():Map<Int, CharacterData> {
        try {
            val characterJson = BaseApplication.appContext.resources.assets.open("immutable/gameCharacters.json")
                .bufferedReader().use { it.readText() }
            val list = GsonUtil.fromJson(characterJson, Array<CharacterData>::class.java) ?: return mapOf()
            return list.associateBy { it.id }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
            return mapOf()
        }
    }

    fun getCharacterUnit(characterId:Int) : String {
        return characterMap[characterId]?.unit ?: ""

    }

    fun getCharacterName(characterId:Int) : String {
        return characterMap[characterId]?.firstName + characterMap[characterId]?.givenName
    }

}