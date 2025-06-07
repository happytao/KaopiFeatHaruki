package com.haruki.kaopifeatharuki.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentAboutBinding
import com.haruki.kaopifeatharuki.viewmodel.AboutViewModel

class AboutFragment:BaseFragment<FragmentAboutBinding,AboutViewModel>() {
    override val mViewModel: AboutViewModel by viewModels()

    override fun getLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }

    override fun initView() {

    }

    override fun initData() {

    }
}