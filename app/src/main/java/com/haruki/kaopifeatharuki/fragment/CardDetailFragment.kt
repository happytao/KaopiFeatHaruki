package com.haruki.kaopifeatharuki.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.adapter.CardDetailViewpagerAdapter
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardDetailBinding
import com.haruki.kaopifeatharuki.util.imageviewer.showViewer
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel

class CardDetailFragment: BaseFragment<FragmentCardDetailBinding, CardViewModel>() {
    companion object {
        private const val TAG = "CardDetailFragment"
    }
    override val mViewModel: CardViewModel by viewModels({requireActivity()})

    private val adapter by lazy {
        CardDetailViewpagerAdapter()
    }

    override fun getLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCardDetailBinding {
        return FragmentCardDetailBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        mBinding.detailViewPager.adapter = adapter
        adapter.submitList(mViewModel.currentCardList)
        mBinding.detailViewPager.post {
            mBinding.detailViewPager.setCurrentItem(mViewModel.selectPosition, false)
        }

        adapter.setOnItemClickListener{ adapter, view, pos ->
            Log.i(TAG,"setOnItemClickListener $pos")
//            findNavController().popBackStack()
        }




    }

    override fun initData() {

    }


}