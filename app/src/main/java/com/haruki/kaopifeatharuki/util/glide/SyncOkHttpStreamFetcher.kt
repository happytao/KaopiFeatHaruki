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
import com.haruki.kaopifeatharuki.application.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Dispatcher
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.coroutines.CoroutineContext

class SyncOkHttpStreamFetcher@JvmOverloads constructor(
    private val client: Call.Factory,
    private val url: GlideUrl,
): OkHttpStreamFetcher(client, url) {

    companion object {
        private const val TAG = "SyncOkHttpStreamFetcher"
        private val mutex = Mutex()
        private var encodeNum = 0
    }

    private var callback: DataFetcher.DataCallback<in InputStream>? = null

    private var response: Response? = null

    private val coroutineScope by lazy {
        CoroutineScope(Dispatchers.IO)
    }

    private var glideStream:InputStream? = null
    private var localSaveStream:ChannelInputStream? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        try {

            this@SyncOkHttpStreamFetcher.callback = callback
            coroutineScope.launch {
                mutex.withLock {
                    try {
                        Log.i(TAG,"start loadData url:$url")
                        val requestBuilder = Request.Builder().url(url.toStringUrl())
                        for ((key, value) in url.headers) {
                            requestBuilder.addHeader(key, value!!)
                        }
                        val request = requestBuilder.build()

                        val call = client.newCall(request)
                        response = call.execute()
                        Log.i(TAG,"loadedData url:$url")
                    } catch (e: Exception) {
                        Log.e(TAG,"okhttp request failed")
                        Log.e(TAG,Log.getStackTraceString(e))
                        this@SyncOkHttpStreamFetcher.callback?.onLoadFailed(e)
                        response?.close()
                        return@launch
                    }
                }

                if (response?.isSuccessful == true) {
                    launch {
                        try {
                            val responseBody = response!!.body
                            shareInputStream(responseBody!!.byteStream(), currentCoroutineContext())
                            val contentLength = Preconditions.checkNotNull(responseBody).contentLength()
                            val stream = ContentLengthInputStream.obtain(glideStream!!, contentLength)
                            callback.onDataReady(stream)
                            delay(1000)
                            saveToLocal(url.toStringUrl(), localSaveStream!!)
                        } catch (e: Exception) {
                            Log.e(TAG,"load data handle failed")
                            Log.e(TAG,Log.getStackTraceString(e))
                            this@SyncOkHttpStreamFetcher.callback?.onLoadFailed(e)
                        }
                    }

                } else {
                    response?.code?.let { callback.onLoadFailed(HttpException(response?.message, it)) }
                }
            }

        } catch (e: Exception) {
            this.callback?.onLoadFailed(e)
        }
    }

    private fun shareInputStream(
        byteStream: InputStream,
        scopeContext: CoroutineContext
    ) {

        val glideChannel = Channel<ByteArray>(50 * 1024)
        val localChannel = Channel<ByteArray>(50 * 1024)
        glideStream = ChannelInputStream(glideChannel)
        localSaveStream = ChannelInputStream(localChannel)
        // 启动协程读取原始流并写入两个输出流
        val scope = CoroutineScope(scopeContext)
        scope.launch {
            try {
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (true) {
                    val bytesRead = byteStream.read(buffer)
                    if (bytesRead == -1) break

                    val packet = buffer.copyOf(bytesRead)
                    glideChannel.send(packet)
                    localChannel.send(packet)
                }
            } catch (e: Exception) {
                Log.e(TAG,"sharedInputStream failed")
                Log.e(TAG,Log.getStackTraceString(e))
                localSaveStream!!.dispatchThrowable(e)
            } finally {
                glideChannel.close()
                localChannel.close()
                byteStream.close()
            }

        }

    }

    private suspend fun saveToLocal(url: String, stream: InputStream) {
        withContext(Dispatchers.IO) {
            val path = ImagePathUtil.extractRelativePath(url)
            val file = ImagePathUtil.ensureFile(BaseApplication.appContext, path)
            val tempFile = File(file.absolutePath + ".temp")
            try {
                encodeNum++
                Log.d(TAG,"start encode num:$encodeNum $url")
                stream.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                encodeNum--
                Log.d(TAG,"end encode num:$encodeNum $url")
                tempFile.renameTo(file)
            } catch (e: Exception) {
                Log.e(TAG ,"save local img failed")
                Log.e(TAG, Log.getStackTraceString(e))
            }finally {
                tempFile.delete()
                stream.close()
            }
        }

    }

    override fun cleanup() {
        Log.i(TAG,"cleanup:$url")
        super.cleanup()
        this.callback = null
        response?.close()
        glideStream?.close()
        localSaveStream?.close()
    }



    override fun onFailure(call: Call, e: IOException) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e)
        }

        callback!!.onLoadFailed(e)
    }



}