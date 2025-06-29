package com.haruki.kaopifeatharuki.util.glide

import android.content.Context
import android.net.Uri
import android.util.Log
import com.haruki.kaopifeatharuki.application.BaseApplication
import com.haruki.kaopifeatharuki.util.ConstUtil.HARUKI_ASSET_URL_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.SEKAI_VIEWER_ASSET_URL_PREFIX
import java.io.File
import java.io.IOException

object ImagePathUtil {
    private const val TAG = "ImagePathUtil"

    private val ASSET_URL_PREFIXES = listOf(
        HARUKI_ASSET_URL_PREFIX,
        SEKAI_VIEWER_ASSET_URL_PREFIX
    )


    fun extractRelativePath(url: String): String {
        return try {
            // 直接检查前缀列表（跳过URI解析）
            ASSET_URL_PREFIXES.firstOrNull { url.startsWith(it) }?.let { prefix ->
                return url.substring(prefix.length).removePrefix("/")
            }
            // 无匹配前缀时的备用方案
            url.substringAfterLast("net/")  // 处理其他含net的域名
                .substringAfterLast("com/") // 处理.com域名
                .removePrefix("/")
        } catch (e: Exception) {
            Log.e(TAG, "URL parse error: ${e.message}")
            // 终极回退方案：取最后一个/后的内容
            url.substringAfterLast("/")
        }
    }


    /**
     * 获取文件对象并确保父目录存在
     * @param context Context对象
     * @param relativePath 相对路径（如："cn-assets/startapp/character.png"）
     * @return 文件对象
     * @throws IOException 当目录创建失败时
     */
    @Throws(IOException::class)
    fun ensureFile(context: Context, relativePath: String): File {
        val file = File(context.filesDir, relativePath)
        val parent = file.parentFile ?: return file

        if (!parent.exists() && !parent.mkdirs()) {
            throw IOException("Failed to create directory: ${parent.absolutePath}")
        }

        return file
    }

    /**
     * 根据URL获取本地文件（仅当文件存在时返回）
     * @param url 原始URL（用于提取相对路径）
     * @return 存在的文件对象，否则返回null
     */
    fun getLocalFile(url: String): File? {
        val relativePath = extractRelativePath(url) ?: return null
        val file = File(BaseApplication.appContext.filesDir, relativePath)
        return file.takeIf { it.exists() && it.isFile } // 仅当文件存在且是普通文件时返回
    }


    fun cleanLocalFile() {
        BaseApplication.appContext.filesDir.clearDirectory()
    }

    fun File.clearDirectory(): Boolean {
        return takeIf { it.exists() && it.isDirectory }?.let { dir ->
            dir.walkBottomUp()
                .maxDepth(Int.MAX_VALUE)
                .filter { it != dir }
                .onEach { it.delete() }
                .all { !it.exists() }
        } ?: false
    }
}