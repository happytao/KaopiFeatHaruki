package com.haruki.kaopifeatharuki.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.engine.executor.GlideExecutor
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
    }

}