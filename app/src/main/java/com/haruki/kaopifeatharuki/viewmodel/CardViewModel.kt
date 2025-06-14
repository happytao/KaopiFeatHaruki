package com.haruki.kaopifeatharuki.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haruki.kaopifeatharuki.base.BaseViewModel
import com.haruki.kaopifeatharuki.repo.data.CardData
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

    val currentCardList = mutableListOf<CardData>()

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

    fun changeTrainingState() {
        this.isShowAfterTraining = !isShowAfterTraining
        currentCardList.forEach { cardData ->
            cardData.isShowAfterTraining = isShowAfterTraining
        }
        viewModelScope.launch {
            _changeTrainingStateCardList.emit(currentCardList)
        }


    }
}