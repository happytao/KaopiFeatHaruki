package com.haruki.kaopifeatharuki.util.imageviewer

import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.adapter.ItemType
import com.github.iielse.imageviewer.core.ImageLoader
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.core.SimpleDataProvider
import com.github.iielse.imageviewer.core.Transformer
import com.haruki.kaopifeatharuki.util.imageviewer.SimpleTransformer.Companion.provide
import com.bumptech.glide.request.target.Target


fun ImageView.showViewer(url:String) {
    val data = MyData(url)
    val dataList = listOf(data)
    val builder = ImageViewerBuilder(
        context = this.context,
        dataProvider = SimpleDataProvider(data, dataList),
        imageLoader = SimpleImageLoader(),
        transformer = SimpleTransformer()
    )
    builder.show()
}

class SimpleImageLoader : ImageLoader {
    /** 根据自身photo数据加载图片.可以使用其它图片加载框架. */
    override fun load(view: ImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
        val it = (data as MyData).url
        Glide.with(view).load(it)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .placeholder(view.drawable)

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


class MyData(val url:String = ""):Photo {
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