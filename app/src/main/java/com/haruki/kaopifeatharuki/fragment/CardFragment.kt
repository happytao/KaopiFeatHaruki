package com.haruki.kaopifeatharuki.fragment
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.activity.MainActivity
import com.haruki.kaopifeatharuki.adapter.ViewpagerAdapter
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardBinding
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel

class CardFragment: BaseFragment<FragmentCardBinding, CardViewModel>() {
    companion object{
        private const val TAG = "CardFragment"
    }
    override val mViewModel by viewModels<CardViewModel>()


    private val bandNameList by lazy {
        listOf(getString(R.string.card_tab_all),
            getString(R.string.card_tab_virtual_singer),
            getString(R.string.card_tab_leo_need),
            getString(R.string.card_tab_more_more_jump),
            getString(R.string.card_tab_vivid_bad_squad),
            getString(R.string.card_tab_wonderland_showtime),
            getString(R.string.card_tab_25_night_cord))
    }

    override fun getLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentCardBinding {
     return FragmentCardBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        mBinding.viewpager.adapter = ViewpagerAdapter(requireActivity())
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewpager, true, true) { tab, position ->
            tab.text = bandNameList[position]
        }.attach()
        editClearFocus()
        initListener()

    }

    override fun initData() {

    }

    private fun getCurrentCardListFragment(): CardListFragment? {
        val currentItem = mBinding.viewpager.currentItem
        return (mBinding.viewpager.adapter as? ViewpagerAdapter)?.getCurrentFragment(currentItem)
    }

    private fun initListener() {
        mBinding.btnFilter.setOnClickListener {
            Log.i(TAG, "initView: btn_filter click")
            getCurrentCardListFragment()?.headerExpandableToggle()
                ?:Log.e(TAG,"currentCardListFragment null")

        }


    }

    /**
     * 设置当点击搜索框外关闭输入法和取消焦点
     */
    private fun editClearFocus() {
            (requireActivity() as MainActivity).setDispatchTouchEvent { event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    // 判断触摸点是否在输入框外部
                    val touchArea = Rect().apply { mBinding.searchContainer.getGlobalVisibleRect(this) }
                    if (!touchArea.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        mBinding.searchContainer.clearFocus() // 清除焦点
                        hideKeyboard(mBinding.searchContainer) // 隐藏键盘
                    }
                }
            }

    }

    private fun hideKeyboard(view: View) {
        val imm = requireActivity().getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}