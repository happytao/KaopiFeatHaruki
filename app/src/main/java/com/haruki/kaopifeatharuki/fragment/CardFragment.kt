package com.haruki.kaopifeatharuki.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardBinding
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel

class CardFragment: BaseFragment<FragmentCardBinding, CardViewModel>() {
    override val mViewModel by viewModels<CardViewModel>()

    override fun getLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentCardBinding {
     return FragmentCardBinding.inflate(inflater, container, false)
    }

    override fun initView() {

    }

    override fun initData() {

    }
}