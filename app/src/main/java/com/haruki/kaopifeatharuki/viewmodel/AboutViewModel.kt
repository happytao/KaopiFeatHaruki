package com.haruki.kaopifeatharuki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haruki.kaopifeatharuki.base.BaseViewModel
import com.haruki.kaopifeatharuki.util.CardJsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutViewModel:BaseViewModel() {

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
                } catch (e: Exception) {
                    jsonStream.close()
                    Log.e(TAG, "parseJson failed: ", e)
                }
            }
        }


    }
}