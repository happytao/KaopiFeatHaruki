package com.haruki.kaopifeatharuki.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.chad.library.adapter4.R
import com.chad.library.adapter4.fullspan.FullSpanAdapterType
import com.chad.library.adapter4.loadState.LoadState


class CardListLoadMoreAdapter : TrailingLoadStateAdapter<CardListLoadMoreAdapter.TrailingLoadStateVH>(),
    FullSpanAdapterType {



    class TrailingLoadStateVH(
        parent: ViewGroup,
        view: View = LayoutInflater.from(parent.context).inflate(R.layout.brvah_trailing_load_more, parent, false)
    ) : RecyclerView.ViewHolder(view) {
        val loadCompleteView: View = itemView.findViewById(R.id.load_more_load_complete_view)
        val loadingView: View = itemView.findViewById(R.id.load_more_loading_view)
        val loadFailView: View = itemView.findViewById(R.id.load_more_load_fail_view)
        val loadEndView: View = itemView.findViewById(R.id.load_more_load_end_view)
    }

    override fun onCreateViewHolder( parent: ViewGroup, loadState: LoadState): TrailingLoadStateVH {
        return TrailingLoadStateVH(
            parent
        ).apply {
            loadFailView.setOnClickListener {
                // 失败重试点击事件
                // retry when loaded failed.
                invokeFailRetry()
            }
            loadCompleteView.setOnClickListener {
                // 加载更多，手动点击事件
                // manual click to load more.
                invokeLoadMore()
            }
        }
    }

    /**
     * bind LoadState
     *
     * 绑定加载状态
     */
    override fun onBindViewHolder(holder: TrailingLoadStateVH, loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                if (loadState.endOfPaginationReached) {
                    holder.loadCompleteView.visibility = View.GONE
                    holder.loadingView.visibility = View.GONE
                    holder.loadFailView.visibility = View.GONE
                    holder.loadEndView.visibility = View.VISIBLE
                } else {
                    holder.loadCompleteView.visibility = View.VISIBLE
                    holder.loadingView.visibility = View.GONE
                    holder.loadFailView.visibility = View.GONE
                    holder.loadEndView.visibility = View.GONE
                }
            }
            is LoadState.Loading -> {
                holder.loadCompleteView.visibility = View.GONE
                holder.loadingView.visibility = View.VISIBLE
                holder.loadFailView.visibility = View.GONE
                holder.loadEndView.visibility = View.GONE
            }
            is LoadState.Error -> {
                holder.loadCompleteView.visibility = View.GONE
                holder.loadingView.visibility = View.GONE
                holder.loadFailView.visibility = View.VISIBLE
                holder.loadEndView.visibility = View.GONE
            }
            is LoadState.None -> {
                holder.loadCompleteView.visibility = View.GONE
                holder.loadingView.visibility = View.GONE
                holder.loadFailView.visibility = View.GONE
                holder.loadEndView.visibility = View.GONE
            }
        }
    }

    override fun getStateViewType(loadState: LoadState): Int = R.layout.brvah_trailing_load_more


}