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

    var currentPosition = 0


    private val cardRepo: CardDBDataRepoImp by lazy {
        CardDBDataRepoImp(CardDataBase.getDatabase(mContext).cardDBDataDao())
    }

    var isShowAfterTraining:Boolean = true
        private set

    fun loadCardList(pageSize: Int,pageIndex: Int) {
        Log.i(TAG, "loadCardList pageSize:$pageSize pageIndex:$pageIndex")
        viewModelScope.launch {
            cardRepo.getAllCardDBData(pageSize,pageIndex).collect{ cardDBDataList ->
                Log.i(TAG,"loadCardList: ${cardDBDataList.size}")
                val cardDataList = mutableListOf<CardData>()
                cardDBDataList.forEach { cardDBData ->
                    cardDataList.add(CardData(cardDBData, isShowAfterTraining))
                    currentCardList.add(CardData(cardDBData, isShowAfterTraining))
                }
                _cardList.emit(cardDataList)
            }
        }
    }

    fun loadCardById(id: Int) {
        viewModelScope.launch {
            cardRepo.getCardDBDataById(id).collect{ cardDBData ->
                val cardData = CardData(cardDBData, isShowAfterTraining)
                _cardDataById.emit(cardData)
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