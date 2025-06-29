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

    private val _restoreEvent = MutableSharedFlow<List<CardData>>()
    val restoreEvent = _restoreEvent.asSharedFlow()

    val currentCardList = mutableListOf<CardData>()

    val cardListBackUpForSearch = mutableListOf<CardData>()

    var filterParam:CardFilterParam? = null

    val isFilterMode:Boolean
        get() = filterParam != null && !filterParam!!.isInitState()

    var currentPosition = 0

    var selectPosition = 0

    var cardListCurrentPageIndex = 0

    private var currentLoadType:LoadType = LoadType.LOAD_ALL



    private val cardRepo: CardDBDataRepoImp by lazy {
        CardDBDataRepoImp(CardDataBase.getDatabase(mContext).cardDBDataDao(),
            CardDataBase.getDatabase(mContext).cardSkillDBDataDao())
    }

    var isShowAfterTraining:Boolean = true
        private set

    fun loadCardList(pageSize: Int,pageIndex: Int) {
        Log.i(TAG, "loadCardList pageSize:$pageSize pageIndex:$pageIndex")
        currentLoadType = LoadType.LOAD_ALL
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
        currentLoadType = LoadType.LOAD_SEARCH
        viewModelScope.launch {
            cardRepo.getCardDBDataById(id).collect{ cardData ->
                cardListBackUpForSearch.clear()
                cardListBackUpForSearch.addAll(currentCardList.map { it.copy() })
                currentCardList.clear()
                currentCardList.add(cardData)
                _cardDataById.emit(cardData.copy().apply { this.isShowAfterTraining = this@CardViewModel.isShowAfterTraining })
            }
        }
    }

    fun loadCardByAllFilterParam(pageSize: Int, pageIndex: Int) {
        if(filterParam == null) {
            return
        }
        currentLoadType = LoadType.LOAD_FILTER
        if(pageIndex == 0) currentCardList.clear()
        val filterParam = filterParam!!
        val sortedProperties = when(filterParam.sortedProperty) {
            "release_time" -> "releaseAt"
            "rarity" -> "cardRarityType"
            "id" -> "id"
            "power" -> "basePower"
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
                Log.i(TAG,"loadCardByAllFilterParam: ${cardDataList.map { "id:" + it.id + "power:" + it.basePower}}")
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
        if(filterParam?.isInitState() == true) {
            currentLoadType = LoadType.LOAD_FILTER
        } else {
            currentLoadType = LoadType.LOAD_ALL
        }
        viewModelScope.launch {
//            val newList = mutableListOf<CardData>()
//            currentCardList.forEach { cardData ->
//                newList.add(cardData.copy())
//            }
            currentCardList.clear()
            currentCardList.addAll(cardListBackUpForSearch.map { it.copy() })
            val newList2 = currentCardList.map{
                it.copy()
            }
            _restoreEvent.emit(newList2)
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
        cardListBackUpForSearch.forEach { cardData ->
            cardData.isShowAfterTraining = isShowAfterTraining
        }
        Log.i(TAG,"currentCardList isShowAfter: ${currentCardList[0].isShowAfterTraining}")

    }

    fun loadMore() {
        cardListCurrentPageIndex += 1
        when(currentLoadType) {
            LoadType.LOAD_ALL -> {
                loadCardList(10, cardListCurrentPageIndex)
            }
            LoadType.LOAD_FILTER -> {
                loadCardByAllFilterParam(10, cardListCurrentPageIndex)
            }
            LoadType.LOAD_SEARCH -> {

            }
        }
    }


    private enum class LoadType{
        LOAD_ALL, //加载全列表
        LOAD_FILTER, //根据过滤参数加载列表
        LOAD_SEARCH //根据搜索数据加载列表
    }
}