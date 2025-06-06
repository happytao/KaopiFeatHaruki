package com.haruki.kaopifeatharuki.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.color.MaterialColors
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.base.BaseActivity
import com.haruki.kaopifeatharuki.databinding.ActivityMainBinding
import com.haruki.kaopifeatharuki.util.dp
import com.haruki.kaopifeatharuki.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {
    override val mViewModel by viewModels<MainViewModel>()


    override fun getLayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.drawLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = v.layoutParams as? ViewGroup.MarginLayoutParams
            params?.setMargins(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




    }

    override fun initData() {

    }
}