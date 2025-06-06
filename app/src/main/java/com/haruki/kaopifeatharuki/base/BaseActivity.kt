package com.haruki.kaopifeatharuki.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding, VM:BaseViewModel>:AppCompatActivity() {
    lateinit var mBinding :VB
    protected abstract val mViewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = getLayout()
        setContentView(mBinding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        initView()
        initData()
    }

    abstract fun getLayout():VB

    abstract fun initView()

    abstract fun initData()
}