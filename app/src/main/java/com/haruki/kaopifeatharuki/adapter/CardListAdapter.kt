package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter4.BaseDifferAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.material.chip.Chip
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.adapter.CharacterChipAdapter.Companion
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.util.dp

class CardListAdapter: BaseDifferAdapter<CardData, QuickViewHolder>(DiffCallback()) {
    companion object {
        private const val TAG = "CardListAdapter"
    }



    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: CardData?) {
        val cardImg = holder.getView<ShapeableImageView>(R.id.iv_item_card)
        val rarityImg = holder.getView<ShapeableImageView>(R.id.iv_item_rarity)
        val attrImg = holder.getView<ShapeableImageView>(R.id.iv_item_attr)
        val idTv = holder.getView<MaterialTextView>(R.id.tv_item_id)
        item?.let {
            Log.i(TAG,"onBindViewHolder ${it.id} ${it.cardRarityType}")
            idTv.text ="id: ${it.id}"
            when(it.cardRarityType) {
                "rarity_4" -> {
                    if(it.isShowAfterTraining) {
                        Glide.with(context).load(R.drawable.rarity_star_4_after_training).override(72.dp.toInt(),18.dp.toInt()).into(rarityImg)
                        Glide.with(context).load(it.afterTrainingThumbnailUrl)
                            .into(cardImg)
                    } else {
                        Glide.with(context).load(R.drawable.rarity_star_4_normal).override(72.dp.toInt(),18.dp.toInt()).into(object : CustomTarget<Drawable>(){
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                rarityImg.setImageDrawable(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }
                        })
                        Glide.with(context).load(it.normalThumbnailUrl)
                            .into(cardImg)
                    }
                }
                "rarity_3" -> {
                    if(it.isShowAfterTraining) {
                        Glide.with(context).load(R.drawable.rarity_star_3_after_training).override(54.dp.toInt(),18.dp.toInt()).into(rarityImg)
                        Glide.with(context).load(it.afterTrainingThumbnailUrl)
                            .into(cardImg)
                    } else {
                        Glide.with(context).load(R.drawable.rarity_star_3_normal).override(54.dp.toInt(),18.dp.toInt()).into(rarityImg)
                        Glide.with(context).load(it.normalThumbnailUrl)
                            .into(cardImg)
                    }
                }
                "rarity_birthday" -> {
                    Glide.with(context).load(R.drawable.rarity_birthday).override(18.dp.toInt(),18.dp.toInt()).into(rarityImg)
                    Glide.with(context).load(it.normalThumbnailUrl)
                        .into(cardImg)
                }
                "rarity_2" -> {
                    Glide.with(context).load(R.drawable.rarity_star_2).override(36.dp.toInt(),18.dp.toInt()).into(rarityImg)
                    Glide.with(context).load(it.normalThumbnailUrl)
                        .into(cardImg)
                }
                "rarity_1" -> {
                    Glide.with(context).load(R.drawable.rarity_star_normal).override(18.dp.toInt(),18.dp.toInt()).into(rarityImg)
                    Glide.with(context).load(it.normalThumbnailUrl)
                        .into(cardImg)
                }

                else -> {}
            }

            when(it.attr) {
                "pure" -> {
                    Glide.with(context).load(R.mipmap.pure_icon).into(attrImg)
                }
                "cute" -> {
                    Glide.with(context).load(R.mipmap.cute_icon).into(attrImg)
                }
                "mysterious"-> {
                    Glide.with(context).load(R.mipmap.mysterious_icon).into(attrImg)
                }
                "cool" -> {
                    Glide.with(context).load(R.mipmap.cool_icon).into(attrImg)
                }
                "happy" -> {
                    Glide.with(context).load(R.mipmap.happy_icon).into(attrImg)
                }
                else -> {}
            }


        }

    }

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: CardData?,
        payloads: List<Any>
    ) {
        if(payloads.isNotEmpty()) {
            Log.i(TAG,"onBindViewHolder payloads")
            val cardImg = holder.getView<ShapeableImageView>(R.id.iv_item_card)
            val rarityImg = holder.getView<ShapeableImageView>(R.id.iv_item_rarity)
            item?.let {
                when(it.cardRarityType) {
                    "rarity_4" -> {
                        if(it.isShowAfterTraining) {
                            Glide.with(context).load(R.drawable.rarity_star_4_after_training).override(72.dp.toInt(),18.dp.toInt()).into(rarityImg)
                            Glide.with(context).load(it.afterTrainingThumbnailUrl)
                                .into(cardImg)
                        } else {
                            Glide.with(context).load(R.drawable.rarity_star_4_normal).override(72.dp.toInt(),18.dp.toInt()).into(
                                object : CustomTarget<Drawable>(){
                                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                        rarityImg.setImageDrawable(resource)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }
                                }
                            )
                            Glide.with(context).load(it.normalThumbnailUrl)
                                .into(cardImg)
                        }
                    }
                    "rarity_3" -> {
                        if(it.isShowAfterTraining) {
                            Glide.with(context).load(R.drawable.rarity_star_3_after_training).override(54.dp.toInt(),18.dp.toInt()).into(rarityImg)
                            Glide.with(context).load(it.afterTrainingThumbnailUrl)
                                .into(cardImg)
                        } else {
                            Glide.with(context).load(R.drawable.rarity_star_3_normal).override(54.dp.toInt(),18.dp.toInt()).into(rarityImg)
                            Glide.with(context).load(it.normalThumbnailUrl)
                                .into(cardImg)
                        }
                    }

                    else -> {}
                }
            }

        } else {
            super.onBindViewHolder(holder, position, item, payloads)
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
//            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem.isShowAfterTraining == newItem.isShowAfterTraining
        }

        override fun getChangePayload(oldItem: CardData, newItem: CardData): Any? {
            return if(oldItem.isShowAfterTraining != newItem.isShowAfterTraining) {
                Log.i(TAG,"getChangePayload")
                newItem.isShowAfterTraining
            } else {
                null
            }
        }
    }

}