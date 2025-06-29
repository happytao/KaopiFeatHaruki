package com.haruki.kaopifeatharuki.util.glide

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import kotlin.math.min

class ChannelInputStream(
    private val channel: Channel<ByteArray>,
    private val scope: CoroutineScope
) : InputStream() {
    private var currentChunk: ByteArray? = null
    private var pos = 0

    private var total = 0


    // 实现三个read方法
    override fun read(): Int  {
        if (!ensureChunk()) return -1
        return currentChunk!![pos++].toInt() and 0xFF
    }

    override fun read(b: ByteArray): Int  {
        return read(b, 0, b.size) // 委托给已实现的read(b,off,len)
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        require(off >= 0 && len >= 0) { "Invalid offset/length" }
        if (len == 0) return 0
        if (!ensureChunk())
            return -1

        val remaining = currentChunk!!.size - pos
        val bytesToCopy = min(len, remaining)
        System.arraycopy(currentChunk!!, pos, b, off, bytesToCopy)
        pos += bytesToCopy
        total+= bytesToCopy
        return bytesToCopy
    }

    override fun available(): Int  {
        return currentChunk?.let { it.size - pos } ?: 0
    }

    override fun close() {
        try {
            channel.close()
        } finally {
            super.close()
        }

    }

    // ============ 内部方法 ============
    private suspend fun loadNextChunk(): Boolean {
        currentChunk = channel.receiveCatching().getOrNull()
        pos = 0
        return currentChunk != null
    }

    private fun ensureChunk(): Boolean = runBlocking {
        if (currentChunk == null || pos >= currentChunk!!.size) {
            loadNextChunk()
        } else true
    }
}