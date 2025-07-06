package com.haruki.kaopifeatharuki.util.imageviewer

import android.graphics.drawable.Drawable
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.adapter.ItemType
import com.github.iielse.imageviewer.core.ImageLoader
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.core.SimpleDataProvider
import com.github.iielse.imageviewer.core.Transformer
import com.bumptech.glide.request.target.Target
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.util.ToastUtil


fun ImageView.showViewer(url:String, thumbnail: String) {
    val data = MyData(url, thumbnail)
    val dataList = listOf(data)
    val builder = ImageViewerBuilder(
        context = this.context,
        dataProvider = SimpleDataProvider(data, dataList),
        imageLoader = SimpleImageLoader(),
        transformer = SimpleTransformer()
    )
    val isNetUrl = url.startsWith("http")
    if(isNetUrl) {
        ToastUtil.showToast(this.context, this.context.getString(R.string.start_loading_original_Image))
    }
    builder.show()
}

class SimpleImageLoader : ImageLoader {
    /** 根据自身photo数据加载图片.可以使用其它图片加载框架. */
    override fun load(view: ImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
        val it = (data as MyData).url
        val isNetUrl = it.startsWith("http")
        val thumbnailUrl = data.thumbnail
        val thumbnail = if(isNetUrl)
            Glide.with(view).load(thumbnailUrl)
        else
            null
        Glide.with(view).load(it)
            .thumbnail(thumbnail)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .placeholder(view.drawable)
            .addListener(object : RequestListener<Drawable> {
                private val isNetUrl = isNetUrl
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    ToastUtil.showToast(view.context, view.context.getString(R.string.load_failed))
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if(this.isNetUrl) {
                        ToastUtil.showToast(view.context, view.context.getString(R.string.load_success))
                    }
                    return false
                }

            })
            .into(view)
    }
}

class SimpleTransformer : Transformer {
    override fun getView(key: Long): ImageView? = provide(key)

    companion object {
        private val transition = HashMap<ImageView, Long>()
        fun put(photoId: Long, imageView: ImageView) {
            require(isMainThread())
            if (!imageView.isAttachedToWindow) return
            imageView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(p0: View) = Unit
                override fun onViewDetachedFromWindow(p0: View) {
                    transition.remove(imageView)
                    imageView.removeOnAttachStateChangeListener(this)
                }
            })
            transition[imageView] = photoId
        }

        private fun provide(photoId: Long): ImageView? {
            transition.keys.forEach {
                if (transition[it] == photoId)
                    return it
            }
            return null
        }
    }
}


class MyData(val url:String = "", val thumbnail:String = ""):Photo {
    override fun id(): Long {
        return 0
    }

    override fun itemType(): Int {
        return ItemType.PHOTO
    }

}

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}