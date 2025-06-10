package com.haruki.kaopifeatharuki.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter.OnTrailingListener
import com.haruki.kaopifeatharuki.adapter.CardListAdapter
import com.haruki.kaopifeatharuki.adapter.decoration.StaggeredGridSpacingDecoration
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
import com.haruki.kaopifeatharuki.util.dp
import com.haruki.kaopifeatharuki.util.observe
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel


class CardListFragment: BaseFragment<FragmentCardListBinding, CardViewModel>() {
    override val mViewModel: CardViewModel by viewModels()

    private var band: String?= null

    private val adapter: CardListAdapter by lazy {
        CardListAdapter()
    }
    private var adapterHelper:QuickAdapterHelper? = null

    private var cardListCurrentPageIndex = 0


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
        val layoutManager = StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL)
        mBinding.recyclerView.layoutManager = layoutManager
        setCardListTrailingLoad()

    }

    override fun initData() {
        band = arguments?.getString(ARG_BAND)

        band?.let {
            when(it) {
                BAND_ALL -> {
                    mBinding.tvTest.text = ""
                    mViewModel.loadCardList(20,cardListCurrentPageIndex)
                }

                BAND_VIRTUAL_SINGER -> {
                    mBinding.tvTest.text = "虚拟歌手"
                }

                BAND_LEO_NEED -> {
                    mBinding.tvTest.text = "雷欧尼"
                }

                BAND_MORE_MORE_JUMP -> {
                    mBinding.tvTest.text = "摸摸酱"
                }

                BAND_VIVID_BAD_SQUAD -> {
                    mBinding.tvTest.text = "VBS"
                }

                BAND_WONDERLAND_SHOWTIME -> {
                    mBinding.tvTest.text = "WS"
                }

                BAND_25_NIGHT_CORD -> {
                    mBinding.tvTest.text = "25时"
                }

                else -> {
                    mBinding.tvTest.text = it
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



    }

    private fun setCardListTrailingLoad() {
        adapterHelper = QuickAdapterHelper.Builder(adapter)
            .setTrailingLoadStateAdapter(object: TrailingLoadStateAdapter.OnTrailingListener {
                override fun onFailRetry() {
                    Log.e(TAG, "TrailingLoad onFailRetry")
                }

                override fun onLoad() {
                    Log.i(TAG, "TrailingLoad onLoad")
                    cardListCurrentPageIndex += 1
                    mViewModel.loadCardList(20,cardListCurrentPageIndex)
                }

            }).build()
        adapterHelper?.trailingLoadStateAdapter?.isAutoLoadMore = false

        mBinding.recyclerView.adapter = adapterHelper?.adapter

    }
}