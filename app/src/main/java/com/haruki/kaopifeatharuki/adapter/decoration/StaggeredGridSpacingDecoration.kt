package com.haruki.kaopifeatharuki.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredGridSpacingDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val layoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = layoutParams.spanIndex // 当前 item 所在的列索引

        if (includeEdge) {
            // 左右间距分配逻辑
            outRect.left = spacing - spanIndex * spacing / spanCount
            outRect.right = (spanIndex + 1) * spacing / spanCount
            // 顶部间距（首行额外处理）
            if (position < spanCount) outRect.top = spacing
            outRect.bottom = spacing
        } else {
            // 无边缘间距的分配逻辑
            outRect.left = spanIndex * spacing / spanCount
            outRect.right = spacing - (spanIndex + 1) * spacing / spanCount
            if (position >= spanCount) outRect.top = spacing
        }
    }
}