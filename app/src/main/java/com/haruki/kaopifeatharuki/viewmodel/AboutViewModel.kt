package com.haruki.kaopifeatharuki.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haruki.kaopifeatharuki.base.BaseViewModel
import com.haruki.kaopifeatharuki.repo.parser.BaseJsonParser
import com.haruki.kaopifeatharuki.repo.parser.CardJsonParser
import com.haruki.kaopifeatharuki.repo.parser.CardSkillJsonParser
import com.haruki.kaopifeatharuki.repo.parser.ClothesJsonParser
import com.haruki.kaopifeatharuki.repo.parser.GachaJsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutViewModel : BaseViewModel() {

    private val _importJsonState = MutableStateFlow(false)
    val importJsonState = _importJsonState.asStateFlow()

    private val _clearDataBaseState = MutableStateFlow(false)
    val clearDataBaseState = _clearDataBaseState.asStateFlow()

    companion object {
        private const val TAG = "AboutViewModel"
    }

    fun parseJson(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            val importTasks = listOf(
                "cards.json" to { CardJsonParser(context) },
                "skills.json" to { CardSkillJsonParser(context) },
                "costume3ds.json" to { ClothesJsonParser(context) },
                "gachas.json" to { GachaJsonParser(context) })

            importTasks.map { (fileName, createParser) ->
                async {
                    executeImportTask(context, fileName, createParser)
                }
            }.awaitAll()
            _importJsonState.value = true
        }


    }


    fun clearDatabase() {
        val result = mContext.deleteDatabase("game_database")
        _clearDataBaseState.value = result

    }

    private suspend fun executeImportTask(
        context: Context, fileName: String, createParser: () -> BaseJsonParser<*,*>
    ) {
        val jsonStream = context.resources.assets.open(fileName)
        try {
            val parser = createParser()

            val logTag = parser::class.simpleName ?: "JsonParser"
            parser.setProgressListener { progress, isFinished ->
                Log.i(TAG, "$logTag progress: $progress")
                if (isFinished) Log.i(TAG, "$logTag isFinished")
            }
            parser.importJson(jsonStream)
        } catch (e: Exception) {
            Log.e(TAG, "parseJson failed: $fileName", e)
        } finally {
            jsonStream.close()
        }

    }
}