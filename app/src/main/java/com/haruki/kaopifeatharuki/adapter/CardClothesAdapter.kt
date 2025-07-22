package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.repo.data.clothes.ClothesData
import com.haruki.kaopifeatharuki.util.loadImage

class CardClothesAdapter: BaseQuickAdapter<ClothesData, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ClothesData?) {
        val ivClothes = holder.getView<ShapeableImageView>(R.id.iv_card_clothes)
        val clothesPart = holder.getView<TextView>(R.id.tv_clothes_part)
        ivClothes.loadImage(item!!.displayClothesUrl)
        val clothesPartName = when(item.partType) {
            "head" -> "头饰"
            "body" -> "服装"
            "hair" -> "发型"
            else -> ""
        }
        clothesPart.text = clothesPartName
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_card_clothes,parent)
    }

}