package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseDifferAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.haruki.kaopifeatharuki.R
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
            idTv.text ="id: ${it.id}"
            when(it.cardRarityType) {
                "rarity_4" -> {
                    if(it.isShowAfterTraining) {
                        Glide.with(context).load(R.drawable.rarity_star_4_after_training).override(72.dp.toInt(),18.dp.toInt()).into(rarityImg)
                        Glide.with(context).load(it.afterTrainingThumbnailUrl)
                            .into(cardImg)
                    } else {
                        Glide.with(context).load(R.drawable.rarity_star_4_normal).override(72.dp.toInt(),18.dp.toInt()).into(rarityImg)
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
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CardData, newItem: CardData): Any? {
            return if(oldItem.isShowAfterTraining != newItem.isShowAfterTraining) {
                "image_change"
            } else {
                return null
            }
        }
    }

}