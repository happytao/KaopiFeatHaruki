package com.haruki.kaopifeatharuki.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding, VM:BaseViewModel>:Fragment() {
    protected lateinit var mBinding :VB
    protected abstract val mViewModel:VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getLayout(inflater, container)
        return mBinding.root
    }

    abstract fun getLayout(inflater: LayoutInflater, container: ViewGroup?):VB

    abstract fun initView()

    abstract fun initData()
}