package com.haruki.kaopifeatharuki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardListBinding
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_25_NIGHT_CORD
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_ALL
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_LEO_NEED
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_MORE_MORE_JUMP
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_VIRTUAL_SINGER
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_VIVID_BAD_SQUAD
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_WONDERLAND_SHOWTIME
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel


class CardListFragment: BaseFragment<FragmentCardListBinding, CardViewModel>() {
    override val mViewModel: CardViewModel by viewModels({requireParentFragment()})

    private var band: String?= null

    companion object{
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

    }

    override fun initData() {
        band = arguments?.getString(ARG_BAND)

        band?.let {
            when(it) {
                BAND_ALL -> {
                    mBinding.tvTest.text = "全部"
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
    }
}