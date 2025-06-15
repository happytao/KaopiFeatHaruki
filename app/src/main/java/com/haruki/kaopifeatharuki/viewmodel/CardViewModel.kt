package com.haruki.kaopifeatharuki.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haruki.kaopifeatharuki.base.BaseViewModel
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.repo.data.CardFilterParam
import com.haruki.kaopifeatharuki.repo.database.CardDBDataRepoImp
import com.haruki.kaopifeatharuki.repo.database.CardDataBase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CardViewModel: BaseViewModel() {

    companion object {
        private const val TAG = "CardViewModel"
    }

    private val _cardList = MutableSharedFlow<List<CardData>>()
    val cardList = _cardList.asSharedFlow()

    private val _changeTrainingStateCardList = MutableSharedFlow<List<CardData>>()
    val changeTrainingStateCardList = _changeTrainingStateCardList.asSharedFlow()

    private val _cardDataById = MutableSharedFlow<CardData>()
    val cardDataById = _cardDataById.asSharedFlow()

    private val _restoreEvent = MutableSharedFlow<Unit>()
    val restoreEvent = _restoreEvent.asSharedFlow()

    val currentCardList = mutableListOf<CardData>()

    var filterParam:CardFilterParam? = null

    val isFilterMode:Boolean
        get() = filterParam != null && !filterParam!!.isInitState()

    var currentPosition = 0

    var cardListCurrentPageIndex = 0



    private val cardRepo: CardDBDataRepoImp by lazy {
        CardDBDataRepoImp(CardDataBase.getDatabase(mContext).cardDBDataDao(),
            CardDataBase.getDatabase(mContext).cardSkillDBDataDao())
    }

    var isShowAfterTraining:Boolean = true
        private set

    fun loadCardList(pageSize: Int,pageIndex: Int) {
        Log.i(TAG, "loadCardList pageSize:$pageSize pageIndex:$pageIndex")
        if(pageIndex == 0) currentCardList.clear()
        viewModelScope.launch {
            cardRepo.getAllCardDBData(pageSize,pageIndex).collect{ cardDataList ->
                Log.i(TAG,"loadCardList: ${cardDataList.size}")
                val newCardDataList = mutableListOf<CardData>()
                cardDataList.forEach { cardData ->
                    newCardDataList.add(cardData.copy().apply { this.isShowAfterTraining = this@CardViewModel.isShowAfterTraining })
                    currentCardList.add(cardData.copy().apply { this.isShowAfterTraining = this@CardViewModel.isShowAfterTraining })
                }
                _cardList.emit(newCardDataList)
            }
        }
    }

    fun loadCardById(id: Int) {
        viewModelScope.launch {
            cardRepo.getCardDBDataById(id).collect{ cardData ->
                _cardDataById.emit(cardData.copy().apply { this.isShowAfterTraining = this@CardViewModel.isShowAfterTraining })
            }
        }
    }

    fun loadCardByAllFilterParam(pageSize: Int, pageIndex: Int) {
        if(filterParam == null) {
            return
        }
        if(pageIndex == 0) currentCardList.clear()
        val filterParam = filterParam!!
        val sortedProperties = when(filterParam.sortedProperty) {
            "release_time" -> "releaseAt"
            "rarity" -> "cardRarityType"
            "id" -> "id"
            "power" -> "releaseAt"
            else -> "releaseAt"
        }
        Log.i(TAG,"""
            loadCardByAllFilterParam:
            filterCharacterIds -> ${filterParam.filterCharacterIds}
            filterAttrs -> ${filterParam.filterAttrs}
            filterRarities -> ${filterParam.filterRarities}
            sortedProperties -> $sortedProperties
            isDescSort -> ${filterParam.isDescSort}
            pageSize -> $pageSize
            pageIndex -> $pageIndex
        """.trimIndent())
        viewModelScope.launch {
            cardRepo.getCardDBDataByAllParam(filterParam.filterCharacterIds, filterParam.filterAttrs,
                filterParam.filterRarities,filterParam.filterSkillTypes,sortedProperties,filterParam.isDescSort,
                pageSize,pageIndex).collect{ cardDataList ->
                Log.i(TAG,"loadCardByAllFilterParam: ${cardDataList.map { it.id }}")
                val newCardDataList = mutableListOf<CardData>()
                cardDataList.forEach { cardData ->
                    newCardDataList.add(cardData.copy().apply { this.isShowAfterTraining = this@CardViewModel.isShowAfterTraining })
                    currentCardList.add(cardData.copy().apply { this.isShowAfterTraining = this@CardViewModel.isShowAfterTraining })
                }
                _cardList.emit(newCardDataList)
            }

        }
    }

    fun restoreCardList() {
        viewModelScope.launch {
            _restoreEvent.emit(Unit)
        }
    }

    fun changeTrainingState(showList: List<CardData>) {
        this.isShowAfterTraining = !isShowAfterTraining
        val newList = showList.map { cardData ->
            cardData.copy().apply { isShowAfterTraining = this@CardViewModel.isShowAfterTraining }
        }
        viewModelScope.launch {
            _changeTrainingStateCardList.emit(newList)
        }
        currentCardList.forEach { cardData ->
            cardData.isShowAfterTraining = isShowAfterTraining
        }

    }
}