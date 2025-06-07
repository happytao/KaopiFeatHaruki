package com.haruki.kaopifeatharuki.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentMusicBinding
import com.haruki.kaopifeatharuki.viewmodel.MusicViewModel

class MusicFragment:BaseFragment<FragmentMusicBinding, MusicViewModel>() {
    override val mViewModel: MusicViewModel by viewModels()

    override fun getLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentMusicBinding {
        return FragmentMusicBinding.inflate(inflater, container, false)
    }

    override fun initView() {

    }

    override fun initData() {

    }
}