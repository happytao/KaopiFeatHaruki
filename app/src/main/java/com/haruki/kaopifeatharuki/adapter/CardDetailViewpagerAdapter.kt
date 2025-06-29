package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter4.BaseDifferAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.haruki.kaopifeatharuki.databinding.ItemCardDetailBinding
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.util.imageviewer.showViewer

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
        Glide.with(context).load(item!!.displayImgUrl).into(holder.binding.ivDetailCardImg)
        holder.binding.ivDetailCardImg.setOnClickListener {
            holder.binding.ivDetailCardImg.showViewer(item.displayImgUrl)
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




}