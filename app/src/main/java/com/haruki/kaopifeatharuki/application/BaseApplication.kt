package com.haruki.kaopifeatharuki.application

import android.app.Application
import android.content.Context
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