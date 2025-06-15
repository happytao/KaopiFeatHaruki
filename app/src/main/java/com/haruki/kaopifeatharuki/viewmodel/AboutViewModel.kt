package com.haruki.kaopifeatharuki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haruki.kaopifeatharuki.base.BaseViewModel
import com.haruki.kaopifeatharuki.repo.parser.CardJsonParser
import com.haruki.kaopifeatharuki.repo.parser.CardSkillJsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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


        viewModelScope.launch {
            val cardImportResult = async(Dispatchers.IO) {
                val jsonStream = context.resources.assets.open("cards.json")
                try {
                    val parser = CardJsonParser(context)
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
                    parser.importJson(jsonStream)
                } catch (e: Exception) {
                    jsonStream.close()
                    Log.e(TAG, "parseJson failed: ", e)
                }
            }

            cardImportResult.await()
            cardSkillImportResult.await()
            _importJsonState.value = true

        }


    }


    fun clearDatabase() {
        val result = mContext.deleteDatabase("card_database")
        _clearDataBaseState.value = result

    }
}