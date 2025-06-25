package com.haruki.kaopifeatharuki.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.toRoute
import androidx.navigation.ui.NavigationUI
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.base.BaseActivity
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.ActivityMainBinding
import com.haruki.kaopifeatharuki.fragment.CardListFragment
import com.haruki.kaopifeatharuki.navigation.About
import com.haruki.kaopifeatharuki.navigation.CardList
import com.haruki.kaopifeatharuki.navigation.Event
import com.haruki.kaopifeatharuki.navigation.KeepStateNavigator
import com.haruki.kaopifeatharuki.navigation.Music
import com.haruki.kaopifeatharuki.navigation.buildGraph
import com.haruki.kaopifeatharuki.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {
    companion object{
        private const val TAG = "MainActivity"
    }
    override val mViewModel by viewModels<MainViewModel>()

    private var dispatchTouchEventCallback: ((ev: MotionEvent) -> Unit)? = null
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController
    }



    override fun getLayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host)
        navController.navigatorProvider.addNavigator(navigator)
        navController.graph = navController.createGraph(startDestination = CardList, builder = {buildGraph()})
        NavigationUI.setupWithNavController(mBinding.navigationView, navController)
        initListener()
    }


    override fun initData() {

    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.i(TAG,"onConfigurationChanged")
        super.onConfigurationChanged(newConfig)


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        mBinding.toolbar.setNavigationOnClickListener {
            mBinding.drawLayout.open()
        }
        mBinding.navigationView.setNavigationItemSelectedListener { menuItem ->
            mBinding.toolbar.title = menuItem.title

            val destination = when(menuItem.itemId) {
                R.id.card_list -> CardList
                R.id.music -> Music
                R.id.event -> Event
                R.id.about -> About
                else -> CardList
            }
            navController.navigate(destination)
            mBinding.drawLayout.close()
            true
        }

        // 添加目的地变化监听器
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.i(TAG,"DestinationChangedListener ${destination.label} route:${destination.route}")
            // 获取当前目的地的ID
            val destinationId = when (destination.route) {
                CardList::class.qualifiedName -> R.id.card_list
                Music::class.qualifiedName -> R.id.music
                Event::class.qualifiedName -> R.id.event
                About::class.qualifiedName -> R.id.about
                else -> null
            }

            // 如果找到了对应的ID，则更新菜单项的选中状态
            destinationId?.let { id ->
                // 遍历所有菜单项，取消选中状态
                mBinding.navigationView.menu.forEach { menu ->
                    menu.subMenu?.forEach { item ->
                        item.isChecked = false
                    }
                }

                // 设置当前目的地对应的菜单项为选中状态
                mBinding.navigationView.menu.findItem(id)?.isChecked = true

                // 更新Toolbar标题
                mBinding.toolbar.title = mBinding.navigationView.menu.findItem(id)?.title
            }
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

    fun setDispatchTouchEvent(callback: (ev: MotionEvent) -> Unit) {
        dispatchTouchEventCallback = callback
    }

    fun removeDispatchTouchEvent() {
        dispatchTouchEventCallback = null
    }

    override fun onDestroy() {
        removeDispatchTouchEvent()
        super.onDestroy()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        dispatchTouchEventCallback?.invoke(ev)
        return super.dispatchTouchEvent(ev)
    }





    override fun onBackPressed() {
        if(navController.navigatorProvider.getNavigator(KeepStateNavigator::class.java).getBackStackSize() == 1) {
                super.onBackPressed()
        } else {
            navController.popBackStack()
        }


    }
}