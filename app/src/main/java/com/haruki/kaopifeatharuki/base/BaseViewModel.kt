package com.haruki.kaopifeatharuki.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.haruki.kaopifeatharuki.application.BaseApplication

abstract class BaseViewModel:ViewModel() {
    protected val mContext = BaseApplication.appContext
}