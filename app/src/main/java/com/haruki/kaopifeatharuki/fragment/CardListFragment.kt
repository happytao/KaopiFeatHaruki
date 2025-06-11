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

    private val iconFilterChipGroupList = mutableListOf<ChipGroup>()


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
        val layoutManager = QuickGridLayoutManager(requireContext(),4)
        mBinding.recyclerView.layoutManager = layoutManager
        setCardListHeaderAndTrailingLoad()

    }

    override fun initData() {
        band = arguments?.getString(ARG_BAND)

        band?.let {
            when(it) {
                BAND_ALL -> {
                    mBinding.tvTest.text = ""
                    mViewModel.loadCardList(40,cardListCurrentPageIndex)
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

    fun getHeaderViewHolder():CardListHeaderViewAdapter.ViewHolder? {
        val headerAdapter = adapterHelper?.beforeAdapterList?.first() as? CardListHeaderViewAdapter
        return headerAdapter?.getViewHolder()

    }

    fun headerExpandableToggle() {
        Log.i(TAG,"headerExpandableToggle")
        val expandableLayout = getHeaderViewHolder()?.getView<ExpandableLayout>(R.id.expandable_layout)
        expandableLayout?.toggle()?:Log.e(TAG,"expandableLayout null")
    }

    private fun setCardListHeaderAndTrailingLoad() {
        val loadMoreAdapter = CardListLoadMoreAdapter()
        loadMoreAdapter.setOnLoadMoreListener(object: OnTrailingListener{
            override fun onFailRetry() {

            }

            override fun onLoad() {
                Log.i(TAG, "load more onLoad")
                cardListCurrentPageIndex += 1
                mViewModel.loadCardList(40,cardListCurrentPageIndex)
            }

        })
        adapterHelper = QuickAdapterHelper.Builder(adapter)
            .setTrailingLoadStateAdapter(loadMoreAdapter).build()
//        adapterHelper?.trailingLoadStateAdapter?.isAutoLoadMore = false
        adapterHelper?.addBeforeAdapter(CardListHeaderViewAdapter())
        mBinding.recyclerView.adapter = adapterHelper?.adapter

        //过滤图片chip点击变灰
        mBinding.recyclerView.post{
            val chipIconBandGroup =  getHeaderViewHolder()?.getView<ChipGroup>(R.id.chip_icon_band_group)
            val chipIconCharacterGroup =  getHeaderViewHolder()?.getView<ChipGroup>(R.id.chip_icon_character_group)
            val chipIconAttributeGroup =  getHeaderViewHolder()?.getView<ChipGroup>(R.id.chip_icon_attribute_group)

            chipIconBandGroup?.let { iconFilterChipGroupList.add(it) }
            chipIconCharacterGroup?.let { iconFilterChipGroupList.add(it) }
            chipIconAttributeGroup?.let { iconFilterChipGroupList.add(it)  }

            iconFilterChipGroupList.forEach { iconGroup ->
                iconGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                    group.forEach {
                        if(it is Chip) {
                            //将所有icon_chip_group设置取消点击后变灰
                            if(!checkedIds.contains(it.id)) {
                                it.chipIcon?.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                                    setSaturation(0f)
                                })
                            } else {
                                it.chipIcon?.colorFilter = null
                            }
                        }
                    }
                }

            }

        }

    }

}