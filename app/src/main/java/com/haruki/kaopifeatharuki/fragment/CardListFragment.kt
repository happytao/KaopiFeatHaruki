package com.haruki.kaopifeatharuki.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.layoutmanager.QuickGridLayoutManager
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter.OnTrailingListener
import com.haruki.kaopifeatharuki.adapter.CardListAdapter
import com.haruki.kaopifeatharuki.adapter.CardListLoadMoreAdapter
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardListBinding
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_ALL
import com.haruki.kaopifeatharuki.util.ToastUtil
import com.haruki.kaopifeatharuki.util.observe
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel



class CardListFragment: BaseFragment<FragmentCardListBinding, CardViewModel>() {
    override val mViewModel: CardViewModel by viewModels({requireParentFragment()})

    private var band: String?= null

    private val adapter: CardListAdapter by lazy {
        CardListAdapter()
    }
    private var adapterHelper:QuickAdapterHelper? = null

    private var cardListCurrentPageIndex = 0


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
                    mViewModel.loadCardList(10,cardListCurrentPageIndex)

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

        mViewModel.cardDataById.observe(this) { cardData ->
            Log.i(TAG,"cardDataById ${cardData.id}")
            mViewModel.currentPosition = (mBinding.recyclerView.layoutManager as QuickGridLayoutManager)
                .findLastVisibleItemPosition()
            adapterHelper?.trailingLoadState = LoadState.NotLoading(true)
            adapter.submitList(listOf(cardData))
        }

        mViewModel.restoreEvent.observe(this) {
            adapter.submitList(null)
            adapter.addAll(mViewModel.currentCardList)
            adapter.notifyDataSetChanged()
            adapterHelper?.trailingLoadState = LoadState.NotLoading(false)
            mBinding.recyclerView.scrollToPosition(mViewModel.currentPosition)

        }

        mBinding.btnFloating.setOnClickListener {
            mViewModel.changeTrainingState(adapter.items)
            if(mViewModel.isShowAfterTraining) {
                ToastUtil.showToast(requireContext(),"已切换为花后图")
            } else {
                ToastUtil.showToast(requireContext(),"已切换为花前图")
            }
        }



    }



    private fun setCardListHeaderAndTrailingLoad() {
        val loadMoreAdapter = CardListLoadMoreAdapter()
        loadMoreAdapter.setOnLoadMoreListener(object: OnTrailingListener{
            override fun onFailRetry() {

            }

            override fun onLoad() {
                Log.i(TAG, "load more onLoad")
                cardListCurrentPageIndex += 1
                mViewModel.loadCardList(10,cardListCurrentPageIndex)
            }

        })
        adapterHelper = QuickAdapterHelper.Builder(adapter)
            .setTrailingLoadStateAdapter(loadMoreAdapter).build()
        mBinding.recyclerView.adapter = adapterHelper?.adapter



    }

}