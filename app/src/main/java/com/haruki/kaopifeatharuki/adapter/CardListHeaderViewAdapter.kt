package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.BaseSingleItemAdapter
import com.chad.library.adapter4.fullspan.FullSpanAdapterType
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.haruki.kaopifeatharuki.R

class CardListHeaderViewAdapter: BaseSingleItemAdapter<Any, CardListHeaderViewAdapter.ViewHolder>(),
    FullSpanAdapterType {
    private var viewHolder: ViewHolder? = null

    class ViewHolder(view: View): QuickViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, item: Any?) {
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_filter_expandable, parent, false)
        viewHolder = ViewHolder(view)
        return ViewHolder(view)
    }

    fun getViewHolder() = viewHolder


}