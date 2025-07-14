package com.haruki.kaopifeatharuki.util

import android.content.pm.ApplicationInfo
import com.haruki.kaopifeatharuki.application.BaseApplication

object AppConfigUtil {

    fun isDebug():Boolean {
        return (BaseApplication.appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
}