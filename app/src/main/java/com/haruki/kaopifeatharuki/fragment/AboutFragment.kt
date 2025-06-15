package com.haruki.kaopifeatharuki.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentAboutBinding
import com.haruki.kaopifeatharuki.util.ToastUtil
import com.haruki.kaopifeatharuki.util.observe
import com.haruki.kaopifeatharuki.viewmodel.AboutViewModel

class AboutFragment:BaseFragment<FragmentAboutBinding,AboutViewModel>() {
    companion object {
        private const val TAG = "AboutFragment"
    }
    override val mViewModel: AboutViewModel by viewModels()

    override fun getLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        mBinding.btnImportDatabase.setOnClickListener {
            Log.i(TAG,"start parse json")
            mViewModel.parseJson(requireContext())
        }

        mBinding.btnClearDatabase.setOnClickListener {
            mViewModel.clearDatabase()
        }

    }

    override fun initData() {
        mViewModel.importJsonState.observe(this) {
            if(it) {
                ToastUtil.showToast(requireContext(), "数据库导入成功")
            }
        }

        mViewModel.clearDataBaseState.observe(this) {
            if(it) {
                ToastUtil.showToast(requireContext(), "数据库删除成功")
            }
        }


    }
}