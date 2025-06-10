package com.haruki.kaopifeatharuki.util.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class FlowControlGlide:AppGlideModule() {


    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setSourceExecutor(GlideExecutor.newSourceBuilder().setThreadCount(2).setName("flow_control_source").build())
        builder.setLogLevel(Log.DEBUG)
        builder.setLogRequestOrigins(true)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client:OkHttpClient = OkHttpClient.Builder().build()
        registry.replace(GlideUrl::class.java, InputStream::class.java, SyncOkHttpUrlLoader.Factory(client))
    }
}