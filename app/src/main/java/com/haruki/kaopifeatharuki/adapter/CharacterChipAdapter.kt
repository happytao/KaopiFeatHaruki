package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter4.BaseDifferAdapter
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.material.chip.Chip
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.repo.data.CharacterChip

class CharacterChipAdapter: BaseDifferAdapter<CharacterChip, QuickViewHolder>(EntityDiffCallback()) {

    companion object {
        private const val TAG = "CharacterChipAdapter"
    }
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: CharacterChip?) {
        Log.i(TAG,"onBindViewHolder normal")
        val chip = holder.getView<Chip>(R.id.chip)
        if(chip.chipIcon == null) {
            Glide.with(context).load(item?.iconResId).into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    chip.chipIcon = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
        }
        chip.isChecked = item!!.isChecked
    }

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: CharacterChip?,
        payloads: List<Any>
    ) {

        if(payloads.isNotEmpty()) {
            Log.i(TAG,"onBindViewHolder payloads")
            val chip = holder.getView<Chip>(R.id.chip)
            chip.isChecked = item!!.isChecked
            changeFilterColor(chip, item.isChecked)
        } else {
            super.onBindViewHolder(holder, position, item, payloads)
        }

    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_chip, parent)
    }

    private fun changeFilterColor(chip: Chip, isChecked: Boolean) {
        if(!isChecked) {
            chip.chipIcon?.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                setSaturation(0f)
            })
        } else {
            chip.chipIcon?.colorFilter = null
        }
    }

    class EntityDiffCallback: DiffUtil.ItemCallback<CharacterChip>() {
        override fun areItemsTheSame(oldItem: CharacterChip, newItem: CharacterChip): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CharacterChip, newItem: CharacterChip): Boolean {
            return oldItem.isChecked == newItem.isChecked
        }

        override fun getChangePayload(oldItem: CharacterChip, newItem: CharacterChip): Any? {
            return if(oldItem.isChecked != newItem.isChecked) {
                Log.i(TAG,"getChangePayload")
                newItem.isChecked
            } else {
                null
            }
        }
    }
}