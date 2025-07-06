package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseDifferAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.databinding.ItemCardDetailBinding
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.repo.datamanager.CharacterInfoManager
import com.haruki.kaopifeatharuki.util.dp
import com.haruki.kaopifeatharuki.util.imageviewer.showViewer
import com.haruki.kaopifeatharuki.util.loadImage
import com.haruki.kaopifeatharuki.util.loadResImage

class CardDetailViewpagerAdapter:BaseDifferAdapter<CardData, CardDetailViewpagerAdapter.VBViewHolder>(DiffCallback()) {
    companion object {
        private const val TAG = "CardDetailViewpagerAdapter"
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

    class VBViewHolder(var binding: ItemCardDetailBinding): QuickViewHolder(binding.root)

    override fun onBindViewHolder(holder: VBViewHolder, position: Int, item: CardData?) {
        Log.i(TAG,"onBindViewHolder $position")
        holder.binding.ivCardAttr.visibility = View.GONE
        holder.binding.ivDetailCardImg.loadImage(item!!.displaySmallImgUrl,
            loadCallback = { isLoadSuccess ->
                if(isLoadSuccess) {
                    showCardRarity(holder, item)
                    showAttrIcon(holder, item)
                }
            })
        showUnitIcon(holder, item)
        holder.binding.tvCardName.text = item.prefix
        holder.binding.tvCardCharacterName.text = CharacterInfoManager.getCharacterName(item.characterId)
        holder.binding.tvCardId.text = item.id.toString()






        holder.binding.ivDetailCardImg.setOnClickListener {
            holder.binding.ivDetailCardImg.showViewer(item.displayLargeImgUrl, item.displaySmallImgUrl)
        }

    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VBViewHolder {
        val view = ItemCardDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VBViewHolder(view)
    }


    private fun showCardRarity(holder: VBViewHolder, item: CardData?) {
        when(item!!.cardRarityType) {
            "rarity_4" -> {
                if(item.isShowAfterTraining) {
                    Glide.with(context).load(R.drawable.rarity_star_4_after_training_vertical).override(18.dp.toInt(),72.dp.toInt()).into(holder.binding.ivCardRarity)
                } else{
                    Glide.with(context).load(R.drawable.rarity_star_4_normal_vertical).override(18.dp.toInt(),72.dp.toInt()).into(holder.binding.ivCardRarity)
                }
            }

            "rarity_3" -> {
                if(item.isShowAfterTraining) {
                    Glide.with(context).load(R.drawable.rarity_star_3_after_training_vertical)
                        .override(18.dp.toInt(),54.dp.toInt())
                        .into(holder.binding.ivCardRarity)
                } else {
                    Glide.with(context).load(R.drawable.rarity_star_3_normal_vertical)
                        .override(18.dp.toInt(),54.dp.toInt())
                        .into(holder.binding.ivCardRarity)

                }
            }
            "rarity_birthday" -> {
                Glide.with(context).load(R.drawable.rarity_birthday)
                    .override(18.dp.toInt(),18.dp.toInt())
                    .into(holder.binding.ivCardRarity)
            }
            "rarity_2" -> {
                Glide.with(context).load(R.drawable.rarity_star_2_vertical)
                    .override(18.dp.toInt(),36.dp.toInt())
                    .into(holder.binding.ivCardRarity)
            }
            "rarity_1" -> {
                Glide.with(context).load(R.drawable.rarity_star_normal)
                    .override(18.dp.toInt(),18.dp.toInt())
                    .into(holder.binding.ivCardRarity)
            }
            else -> {}

        }
    }

    private fun showAttrIcon(holder: VBViewHolder, item: CardData?) {
        holder.binding.ivCardAttr.visibility = View.VISIBLE
        when(item!!.attr) {
            "pure" -> {
                holder.binding.ivCardAttr.loadResImage(R.mipmap.pure_icon)
                holder.binding.ivAttr.loadResImage(R.mipmap.pure_icon)
                holder.binding.tvAttrKey.text = context.resources.getString(R.string.attr_pure)
            }
            "cute" -> {
                holder.binding.ivCardAttr.loadResImage(R.mipmap.cute_icon)
                holder.binding.ivAttr.loadResImage(R.mipmap.cute_icon)
                holder.binding.tvAttrKey.text = context.resources.getString(R.string.attr_cute)
            }
            "mysterious"-> {
                holder.binding.ivCardAttr.loadResImage(R.mipmap.mysterious_icon)
                holder.binding.ivAttr.loadResImage(R.mipmap.mysterious_icon)
                holder.binding.tvAttrKey.text = context.resources.getString(R.string.attr_mysterious)
            }
            "cool" -> {
                holder.binding.ivCardAttr.loadResImage(R.mipmap.cool_icon)
                holder.binding.ivAttr.loadResImage(R.mipmap.cool_icon)
                holder.binding.tvAttrKey.text = context.resources.getString(R.string.attr_cool)
            }
            "happy" -> {
                holder.binding.ivCardAttr.loadResImage(R.mipmap.happy_icon)
                holder.binding.ivAttr.loadResImage(R.mipmap.happy_icon)
                holder.binding.tvAttrKey.text = context.resources.getString(R.string.attr_happy)
            }
            else -> {}
        }

    }

    private fun showUnitIcon(holder: VBViewHolder, item: CardData?) {
        if(item == null) return
        var unit = item.supportUnit
        if(unit == "none") {
            unit = CharacterInfoManager.getCharacterUnit(item.characterId)
        }

        when(unit) {
            "light_sound" -> {
                holder.binding.ivBandIcon.loadResImage(R.mipmap.logo_light_sound)
            }
            "idol" -> {
                holder.binding.ivBandIcon.loadResImage(R.mipmap.logo_idol)

            }
            "street" -> {
                holder.binding.ivBandIcon.loadResImage(R.mipmap.logo_street)
            }
            "theme_park" -> {
                holder.binding.ivBandIcon.loadResImage(R.mipmap.logo_theme_park)
            }
            "school_refusal" -> {
                holder.binding.ivBandIcon.loadResImage(R.mipmap.logo_school_refusal)
            }
            "piapro" -> {
                holder.binding.ivBandIcon.loadResImage(R.mipmap.logo_piapro)
            }
            else -> {}
        }

    }






}