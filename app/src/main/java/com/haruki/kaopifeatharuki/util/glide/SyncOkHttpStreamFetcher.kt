package com.haruki.kaopifeatharuki.util.glide

import android.util.Log
import com.bumptech.glide.Priority
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import com.bumptech.glide.util.Preconditions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import okhttp3.Call
import okhttp3.Dispatcher
import okhttp3.Request
import java.io.IOException
import java.io.InputStream

class SyncOkHttpStreamFetcher@JvmOverloads constructor(
    private val client: Call.Factory,
    private val url: GlideUrl,
): OkHttpStreamFetcher(client, url) {

    companion object {
        private const val TAG = "SyncOkHttpStreamFetcher"
        private val semaphore = Semaphore(1)
    }

    private var callback: DataFetcher.DataCallback<in InputStream>? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        try {
            runBlocking {
                semaphore.acquire()
                Log.d(TAG,"start loadData url:$url")
                val requestBuilder = Request.Builder().url(url.toStringUrl())
                for ((key, value) in url.headers) {
                    requestBuilder.addHeader(key, value!!)
                }
                val request = requestBuilder.build()
                this@SyncOkHttpStreamFetcher.callback = callback

                val call = client.newCall(request)
                val response = call.execute()
                Log.d(TAG,"loadedData url:$url")
                val responseBody = response.body
                if (response.isSuccessful) {
                    val contentLength = Preconditions.checkNotNull(responseBody).contentLength()
                    val stream = ContentLengthInputStream.obtain(responseBody!!.byteStream(), contentLength)
                    callback.onDataReady(stream)
                } else {
                    callback.onLoadFailed(HttpException(response.message, response.code))
                }
            }

        } catch (e: Exception) {
            this.callback?.onLoadFailed(e)
        }finally {
            semaphore.release()
        }
    }

    override fun cleanup() {
        super.cleanup()
        this.callback = null
    }

    override fun onFailure(call: Call, e: IOException) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e)
        }

        callback!!.onLoadFailed(e)
    }



}