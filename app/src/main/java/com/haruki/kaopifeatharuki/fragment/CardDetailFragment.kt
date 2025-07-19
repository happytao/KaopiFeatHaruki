package com.haruki.kaopifeatharuki.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.haruki.kaopifeatharuki.adapter.CardDetailViewpagerAdapter
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardDetailBinding
import com.haruki.kaopifeatharuki.util.observe
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel

class CardDetailFragment: BaseFragment<FragmentCardDetailBinding, CardViewModel>() {
    companion object {
        private const val TAG = "CardDetailFragment"
    }
    override val mViewModel: CardViewModel by viewModels({requireActivity()})

    private val adapter by lazy {
        CardDetailViewpagerAdapter(mViewModel,this,mBinding.detailViewPager)
    }

    override fun getLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCardDetailBinding {
        return FragmentCardDetailBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        mBinding.detailViewPager.offscreenPageLimit = 2
        mBinding.detailViewPager.adapter = adapter

    }

    override fun initData() {

        val newList = mViewModel.currentCardList.map {
            it.copy()
        }
        adapter.submitList(newList) {
            mBinding.detailViewPager.setCurrentItem(mViewModel.selectPosition, false)
        }
        adapter.setOnItemClickListener{ adapter, view, pos ->
            Log.i(TAG,"setOnItemClickListener $pos")
        }

        mBinding.detailViewPager.registerOnPageChangeCallback(viewpagerChangeCallback)

        mViewModel.cardList.observe(this){ cardList ->
            if(cardList.isEmpty()) return@observe
            adapter.addAll(cardList)
        }


    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            val newList = mViewModel.currentCardList.map {
                it.copy()
            }
            adapter.submitList(newList){
                mBinding.detailViewPager.setCurrentItem(mViewModel.selectPosition, false)
            }

        }
    }

    override fun onDestroy() {
        mBinding.detailViewPager.unregisterOnPageChangeCallback(viewpagerChangeCallback)
        super.onDestroy()
    }

    private val viewpagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mBinding.tvLoading.visibility = View.GONE
        }
    }




}