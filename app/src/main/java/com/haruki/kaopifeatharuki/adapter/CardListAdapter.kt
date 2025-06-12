package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter4.BaseDifferAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.repo.data.CardData

class CardListAdapter: BaseDifferAdapter<CardData, QuickViewHolder>(DiffCallback()) {



    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: CardData?) {
        val cardImg = holder.getView<ShapeableImageView>(R.id.iv_item_card)
        item?.let {
            if(it.isShowAfterTraining) {
                Glide.with(context).load(it.afterTrainingThumbnailUrl)
                    .into(cardImg)
            } else {
                Glide.with(context).load(it.normalThumbnailUrl)
                    .into(cardImg)
            }
        }



    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_card_list, parent)
    }

    class DiffCallback: DiffUtil.ItemCallback<CardData>() {
        override fun areItemsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem.isShowAfterTraining == newItem.isShowAfterTraining
        }


    }

}