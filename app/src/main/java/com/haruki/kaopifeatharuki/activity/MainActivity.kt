package com.haruki.kaopifeatharuki.activity

import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.base.BaseActivity
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.ActivityMainBinding
import com.haruki.kaopifeatharuki.fragment.AboutFragment
import com.haruki.kaopifeatharuki.fragment.CardFragment
import com.haruki.kaopifeatharuki.fragment.EventFragment
import com.haruki.kaopifeatharuki.fragment.MusicFragment
import com.haruki.kaopifeatharuki.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {
    override val mViewModel by viewModels<MainViewModel>()

    private val fragmentList = mutableListOf<BaseFragment<*,*>>()


    override fun getLayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        initListener()
        showFragment<CardFragment>()

    }

    override fun initData() {

    }

    private fun initListener() {
        mBinding.toolbar.setNavigationOnClickListener {
            mBinding.drawLayout.open()
        }
        mBinding.navigationView.setNavigationItemSelectedListener { menuItem ->
            mBinding.navigationView.menu.forEach {
                it.subMenu?.forEach { item ->
                    item.isChecked = false
                }
            }
            menuItem.isChecked = true
            mBinding.toolbar.title = menuItem.title
            changeFragment(menuItem.title.toString())
            mBinding.drawLayout.close()
            true
        }

        mBinding.drawLayout.addDrawerListener(object : DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.md_theme_secondaryContainer)
            }

            override fun onDrawerClosed(drawerView: View) {
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.md_theme_primaryContainer)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })
    }

    private fun changeFragment(fragmentName:String) {
        when(fragmentName) {
            getString(R.string.navigation_draw_func_1) -> {
                showFragment<CardFragment>()
            }
            getString(R.string.navigation_draw_func_2) -> {
                showFragment<MusicFragment>()
            }
            getString(R.string.navigation_draw_func_3) -> {
                showFragment<EventFragment>()
            }
            getString(R.string.navigation_other_about) -> {
                showFragment<AboutFragment>()
            }

            else -> {}
        }

    }



    private inline fun<reified T :BaseFragment<*,*>> showFragment() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        fragmentList.forEach { fragment ->
            if (fragment.isAdded) {
                transaction.hide(fragment)
            }
        }

        val fragment = fragmentList.find { it is T } as? T

        if (fragment != null) {
            transaction.show(fragment)
        } else {
            val newFragment = T::class.java.getDeclaredConstructor().newInstance()
            fragmentList.add(newFragment)
            transaction.add(R.id.fragment_container, newFragment)
        }

        transaction.commit()
    }
}