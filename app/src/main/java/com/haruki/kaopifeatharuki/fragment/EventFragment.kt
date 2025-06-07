package com.haruki.kaopifeatharuki.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentEventBinding
import com.haruki.kaopifeatharuki.viewmodel.EventViewModel

class EventFragment:BaseFragment<FragmentEventBinding, EventViewModel>() {
    override val mViewModel: EventViewModel by viewModels()

    override fun getLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentEventBinding {
        return FragmentEventBinding.inflate(inflater, container, false)
    }

    override fun initView() {

    }

    override fun initData() {

    }
}