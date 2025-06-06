package com.haruki.kaopifeatharuki.base

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding, VM:BaseViewModel>:AppCompatActivity() {
    lateinit var mBinding :VB
    protected abstract val mViewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = getLayout()
        setContentView(mBinding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = v.layoutParams as? ViewGroup.MarginLayoutParams
            params?.setMargins(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        initView()
        initData()
    }

    abstract fun getLayout():VB

    abstract fun initView()

    abstract fun initData()
}