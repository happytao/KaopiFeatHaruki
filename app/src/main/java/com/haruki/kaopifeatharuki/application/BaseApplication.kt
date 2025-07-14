package com.haruki.kaopifeatharuki.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.haruki.kaopifeatharuki.util.AppConfigUtil
import kotlin.properties.Delegates

class BaseApplication: Application() {
    companion object {
        private var _app: BaseApplication by Delegates.notNull()
        val app: BaseApplication get() = _app
        val appContext: Context get() = _app.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        _app = this
        if(AppConfigUtil.isDebug()) {
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )

            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build()
            )
        }
    }

}