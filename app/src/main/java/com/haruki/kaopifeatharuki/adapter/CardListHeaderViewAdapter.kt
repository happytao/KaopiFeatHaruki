package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import com.chad.library.adapter4.BaseSingleItemAdapter
import com.chad.library.adapter4.fullspan.FullSpanAdapterType
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.haruki.kaopifeatharuki.R

class CardListHeaderViewAdapter: BaseSingleItemAdapter<Any, CardListHeaderViewAdapter.ViewHolder>(),
    FullSpanAdapterType {
    private var _viewHolder: ViewHolder? = null
    val viewHolder: ViewHolder? get() = _viewHolder
    private val iconFilterChipGroupList = mutableListOf<ChipGroup>()

    class ViewHolder(view: View): QuickViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, item: Any?) {
        initListener(holder)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_filter_expandable, parent, false)
        _viewHolder = ViewHolder(view)
        return ViewHolder(view)
    }

    private fun initListener(holder: ViewHolder) {
        //过滤图片chip点击变灰
        val chipIconBandGroup =  holder.getView<ChipGroup>(R.id.chip_icon_band_group)
        val chipIconCharacterGroup =  holder.getView<ChipGroup>(R.id.chip_icon_character_group)
        val chipIconAttributeGroup =  holder.getView<ChipGroup>(R.id.chip_icon_attribute_group)
        iconFilterChipGroupList.clear()
        iconFilterChipGroupList.add(chipIconBandGroup)
        iconFilterChipGroupList.add(chipIconCharacterGroup)
        iconFilterChipGroupList.add(chipIconAttributeGroup)

        iconFilterChipGroupList.forEach { iconGroup ->
            iconGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                group.forEach {
                    if(it is Chip) {
                        //将所有icon_chip_group设置取消点击后变灰
                        if(!checkedIds.contains(it.id)) {
                            it.chipIcon?.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                                setSaturation(0f)
                            })
                        } else {
                            it.chipIcon?.colorFilter = null
                        }
                    }
                }
            }
        }
    }

    fun clearListener() {
        iconFilterChipGroupList.forEach { iconGroup ->
            iconGroup.setOnCheckedStateChangeListener(null)
        }
    }
}