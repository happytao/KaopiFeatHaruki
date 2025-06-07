package com.haruki.kaopifeatharuki.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardBinding
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel

class CardFragment: BaseFragment<FragmentCardBinding, CardViewModel>() {
    companion object{
        private const val TAG = "CardFragment"
    }
    override val mViewModel by viewModels<CardViewModel>()

    private val iconFilterChipGroupList = mutableListOf<ChipGroup>()

    override fun getLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentCardBinding {
     return FragmentCardBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        editClearFocus()
        initListener()

    }

    override fun initData() {

    }

    private fun initListener() {
        mBinding.btnFilter.setOnClickListener {
            Log.i(TAG, "initView: btn_filter click")
            mBinding.expandableLayout.toggle()
        }

        iconFilterChipGroupList.add(mBinding.expandLink.chipIconBandGroup)
        iconFilterChipGroupList.add(mBinding.expandLink.chipIconCharacterGroup)
        iconFilterChipGroupList.add(mBinding.expandLink.chipIconAttributeGroup)


        iconFilterChipGroupList.forEach { iconGroup ->
            iconGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                group.forEach {
                    if(it is Chip) {
                        //将所有icon_chip_group设置取消点击后变灰
                        if(!checkedIds.contains(it.id)) {
                            it.chipIcon?.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                                setSaturation(0f)
                            })
                        } else {
                            it.chipIcon?.colorFilter = null
                        }
                    }
                }
            }

        }



    }

    /**
     * 设置当点击搜索框外关闭输入法和取消焦点
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun editClearFocus() {
        mBinding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // 判断触摸点是否在输入框外部
                val touchArea = Rect().apply { mBinding.searchContainer.getGlobalVisibleRect(this) }
                if (!touchArea.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    mBinding.searchContainer.clearFocus() // 清除焦点
                    hideKeyboard(mBinding.searchContainer) // 隐藏键盘
                }
            }
            false
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = requireActivity().getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}