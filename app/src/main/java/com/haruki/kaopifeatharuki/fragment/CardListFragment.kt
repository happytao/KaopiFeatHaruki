package com.haruki.kaopifeatharuki.fragment

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.layoutmanager.QuickGridLayoutManager
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter.OnTrailingListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.adapter.CardListAdapter
import com.haruki.kaopifeatharuki.adapter.CardListHeaderViewAdapter
import com.haruki.kaopifeatharuki.adapter.CardListLoadMoreAdapter
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardListBinding
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_25_NIGHT_CORD
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_ALL
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_LEO_NEED
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_MORE_MORE_JUMP
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_VIRTUAL_SINGER
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_VIVID_BAD_SQUAD
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_WONDERLAND_SHOWTIME
import com.haruki.kaopifeatharuki.util.ToastUtil
import com.haruki.kaopifeatharuki.util.observe
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel
import net.cachapa.expandablelayout.ExpandableLayout


class CardListFragment: BaseFragment<FragmentCardListBinding, CardViewModel>() {
    override val mViewModel: CardViewModel by viewModels()

    private var band: String?= null

    private val adapter: CardListAdapter by lazy {
        CardListAdapter()
    }
    private var adapterHelper:QuickAdapterHelper? = null

    private var cardListCurrentPageIndex = 0



    private val headerViewAdapter by lazy {
        CardListHeaderViewAdapter()
    }

    private var isHeaderViewExpanded = false

    private var isAfterTraining = true


    companion object{
        private const val TAG = "CardListFragment"
        private const val ARG_BAND = "band"


        fun newInstance(band: String?): CardListFragment {
            val fragment = CardListFragment()
            val args = Bundle()
            args.putString(ARG_BAND, band)
            fragment.setArguments(args)
            return fragment
        }
    }




    override fun getLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCardListBinding {
        return FragmentCardListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        val layoutManager = QuickGridLayoutManager(requireContext(),3)
        mBinding.recyclerView.layoutManager = layoutManager
        setCardListHeaderAndTrailingLoad()

    }

    override fun initData() {
        band = arguments?.getString(ARG_BAND)

        band?.let {
            when(it) {
                BAND_ALL -> {
                    mViewModel.loadCardList(15,cardListCurrentPageIndex)

                }


            }
        }

        mViewModel.cardList.observe(this) { cardList ->
            if(cardList.isEmpty()) {
                adapterHelper?.trailingLoadState = LoadState.NotLoading(true)
                ToastUtil.showToast(requireContext(), "没有更多数据")
            } else {
                adapterHelper?.trailingLoadState = LoadState.NotLoading(false)
            }
            if(cardListCurrentPageIndex == 0) {
                adapter.submitList(cardList)
            } else {
                adapter.addAll(cardList)
            }

        }

        mViewModel.changeTrainingStateCardList.observe(this) { cardList ->
            Log.i(TAG,"changeTrainingStateCardList ${cardList.size}")
            adapter.submitList(cardList)
        }

        mBinding.btnFloating.setOnClickListener {
            mViewModel.changeTrainingState()

        }





    }


    fun headerExpandableToggle() {
        Log.i(TAG,"headerExpandableToggle")
        if(isHeaderViewExpanded) {
            headerViewAdapter.clearListener()
            adapterHelper?.clearBeforeAdapters()
        } else {
            adapterHelper?.addBeforeAdapter(headerViewAdapter)
            headerViewAdapter.notifyDataSetChanged()
        }
        isHeaderViewExpanded = !isHeaderViewExpanded

    }

    private fun setCardListHeaderAndTrailingLoad() {
        val loadMoreAdapter = CardListLoadMoreAdapter()
        loadMoreAdapter.setOnLoadMoreListener(object: OnTrailingListener{
            override fun onFailRetry() {

            }

            override fun onLoad() {
                Log.i(TAG, "load more onLoad")
                cardListCurrentPageIndex += 1
                mViewModel.loadCardList(15,cardListCurrentPageIndex)
            }

        })
        adapterHelper = QuickAdapterHelper.Builder(adapter)
            .setTrailingLoadStateAdapter(loadMoreAdapter).build()
        mBinding.recyclerView.adapter = adapterHelper?.adapter



    }

}