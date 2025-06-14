package com.haruki.kaopifeatharuki.util

import com.haruki.kaopifeatharuki.application.BaseApplication
import android.util.TypedValue


val Int.dp: Float
get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    BaseApplication.appContext.resources.displayMetrics
)


