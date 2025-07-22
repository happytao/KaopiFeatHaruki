package com.haruki.kaopifeatharuki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haruki.kaopifeatharuki.base.BaseViewModel
import com.haruki.kaopifeatharuki.repo.parser.CardJsonParser
import com.haruki.kaopifeatharuki.repo.parser.CardSkillJsonParser
import com.haruki.kaopifeatharuki.repo.parser.ClothesJsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutViewModel:BaseViewModel() {

    private val _importJsonState = MutableStateFlow(false)
    val importJsonState = _importJsonState.asStateFlow()

    private val _clearDataBaseState = MutableStateFlow(false)
    val clearDataBaseState = _clearDataBaseState.asStateFlow()

    companion object {
        private const val TAG = "AboutViewModel"
    }

    fun parseJson(context: Context)  {


        viewModelScope.launch(Dispatchers.IO) {
            val cardImportResult = async(Dispatchers.IO) {
                val jsonStream = context.resources.assets.open("cards.json")
                try {
                    val parser = CardJsonParser(context)
                    parser.setProgressListener { progress, isFinished ->
                        Log.i(TAG, "CardJsonParser progress: $progress")
                        if(isFinished) {
                            Log.i(TAG, "CardJsonParser isFinished")
                        }
                    }
                    parser.importJson(jsonStream)
                } catch (e: Exception) {
                    jsonStream.close()
                    Log.e(TAG, "parseJson failed: ", e)
                }
            }

            val cardSkillImportResult = async(Dispatchers.IO) {
                val jsonStream = context.resources.assets.open("skills.json")
                try {
                    val parser = CardSkillJsonParser(context)
                    parser.setProgressListener { progress, isFinished ->
                        Log.i(TAG, "CardSkillJsonParser progress: $progress")
                        if(isFinished) {
                            Log.i(TAG, "CardSkillJsonParser isFinished")
                        }
                    }
                    parser.importJson(jsonStream)
                } catch (e: Exception) {
                    jsonStream.close()
                    Log.e(TAG, "parseJson failed: ", e)
                }
            }

            val clothesImportResult = async(Dispatchers.IO) {
                val jsonStream = context.resources.assets.open("costume3ds.json")
                try {
                    val parser = ClothesJsonParser(context)
                    parser.setProgressListener { progress, isFinished ->
                        Log.i(TAG, "ClothesJsonParser progress: $progress")
                        if(isFinished) {
                            Log.i(TAG, "ClothesJsonParser isFinished")
                        }
                    }
                    parser.importJson(jsonStream)
                } catch (e: Exception) {
                    jsonStream.close()
                    Log.e(TAG, "parseJson failed: ", e)
                }
            }

            awaitAll(cardImportResult, cardSkillImportResult, clothesImportResult)
            _importJsonState.value = true

        }


    }


    fun clearDatabase() {
        val result = mContext.deleteDatabase("game_database")
        _clearDataBaseState.value = result

    }
}