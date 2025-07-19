package com.haruki.kaopifeatharuki.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.loadImage(
    url: String,
    width: Int? = null,
    height: Int? = null,
    thumbnailUrl: String? = null,
    placeHolder: Int? = null,
    error: Int? = null,
    loadCallback: ((Boolean) -> Unit)? = null
) {
    Glide.with(this)
        .load(url)
        .apply {
            // 设置缩略图（仅当thumbnailUrl非空时）
            thumbnailUrl?.let {
                thumbnail(Glide.with(this@loadImage).load(it))
            }
            // 设置尺寸覆盖（仅当width和height都非空时）
            if (width != null && height != null) {
                override(width, height)
            }
            // 设置占位图（仅当非空时）
            placeHolder?.let {
                placeholder(it)
            }
            // 设置错误图（仅当非空时）
            error?.let {
                error(it)
            }
        }
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                loadCallback?.invoke(false)
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                loadCallback?.invoke(true)
                return false
            }
        })
        .into(this)
}

fun ImageView.postLoadImage(
    url: String,
    width: Int? = null,
    height: Int? = null,
    thumbnailUrl: String? = null,
    placeHolder: Int? = null,
    error: Int? = null,
    loadCallback: ((Boolean) -> Unit)? = null
) {
    this.post {
        loadImage(url, width, height, thumbnailUrl, placeHolder, error, loadCallback)
    }
}


fun ImageView.loadResImage(res:Int,
                           width: Int? = null,
                           height: Int? = null) {
    Glide.with(this)
        .load(res)
        .apply {
            if (width != null && height != null) {
                override(width, height)
            }
        }
        .into(this)
}

fun ImageView.postLoadResImage(res:Int,
                               width: Int? = null,
                               height: Int? = null) {
    this.post {
        loadResImage(res,width, height)
    }
}