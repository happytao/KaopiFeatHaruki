package com.haruki.kaopifeatharuki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haruki.kaopifeatharuki.base.BaseViewModel
import com.haruki.kaopifeatharuki.util.CardJsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutViewModel:BaseViewModel() {

    private val _importJsonState = MutableStateFlow(false)
    val importJsonState = _importJsonState.asStateFlow()

    companion object {
        private const val TAG = "AboutViewModel"
    }

    fun parseJson(context: Context)  {
        val parser = CardJsonParser(context)
        val jsonStream = context.resources.assets.open("cards.json")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    parser.importJson(jsonStream)
                    _importJsonState.value = true
                } catch (e: Exception) {
                    jsonStream.close()
                    Log.e(TAG, "parseJson failed: ", e)
                }
            }
        }


    }
}