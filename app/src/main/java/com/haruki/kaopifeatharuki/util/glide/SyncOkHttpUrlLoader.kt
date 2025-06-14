package com.haruki.kaopifeatharuki.util.glide

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.InputStream
import kotlin.concurrent.Volatile

class SyncOkHttpUrlLoader@JvmOverloads constructor(
    private val client: Call.Factory
): OkHttpUrlLoader(client) {

    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        return LoadData(model, SyncOkHttpStreamFetcher(client, model))
    }


    /** The default factory for [OkHttpUrlLoader]s.  */ // Public API.
    class Factory
    /** Constructor for a new Factory that runs requests using a static singleton client.  */ @JvmOverloads constructor(
        private val client: Call.Factory = internalClient!!
    ) :
        ModelLoaderFactory<GlideUrl, InputStream> {
        /**
         * Constructor for a new Factory that runs requests using given client.
         *
         * @param client this is typically an instance of `OkHttpClient`.
         */

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return SyncOkHttpUrlLoader(client)
        }

        override fun teardown() {
            // Do nothing, this instance doesn't own the client.
        }

        companion object {
            @Volatile
            private var internalClient: Call.Factory? = null
                get() {
                    if (field == null) {
                        synchronized(Factory::class.java) {
                            if (field == null) {
                                field = OkHttpClient()
                            }
                        }
                    }
                    return field
                }
        }
    }
}