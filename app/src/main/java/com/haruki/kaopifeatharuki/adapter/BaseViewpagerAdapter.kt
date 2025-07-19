//package com.haruki.kaopifeatharuki.adapter
//
//import android.content.Context
//import android.util.Log
//import android.view.ViewGroup
//import androidx.lifecycle.DefaultLifecycleObserver
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.LifecycleRegistry
//import androidx.recyclerview.widget.DiffUtil
//import androidx.viewpager2.widget.ViewPager2
//import com.chad.library.adapter4.BaseDifferAdapter
//import com.chad.library.adapter4.viewholder.QuickViewHolder
//import com.haruki.kaopifeatharuki.databinding.ItemCardDetailBinding
//import com.haruki.kaopifeatharuki.repo.data.card.CardData
//import com.haruki.kaopifeatharuki.viewmodel.CardViewModel
//
//class BaseViewpagerAdapter(
//    private val mViewModel: CardViewModel,
//    private val lifecycleOwner: LifecycleOwner,
//    private val viewpager:ViewPager2
//): BaseDifferAdapter<CardData, CardDetailViewpagerAdapter.VBViewHolder>(DiffCallback()){
//
//    companion object {
//        private const val TAG = "BaseViewpagerAdapter"
//    }
//
//    protected lateinit var mBinding:ItemCardDetailBinding
//
//    private val lifecycleObserve = object: DefaultLifecycleObserver {
//        override fun onDestroy(owner: LifecycleOwner) {
//            viewpager.unregisterOnPageChangeCallback(viewpagerChangeCallback)
//            super.onDestroy(owner)
//        }
//    }
//
//    private val viewpagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//
//        }
//
//    }
//
//    init {
//        initAdapter()
//    }
//
//
//    private fun initAdapter() {
//        lifecycleOwner.lifecycle.addObserver(lifecycleObserve)
//        viewpager.registerOnPageChangeCallback(viewpagerChangeCallback)
//    }
//
//    class DiffCallback: DiffUtil.ItemCallback<CardData>() {
//        override fun areItemsTheSame(oldItem: CardData, newItem: CardData): Boolean {
//            return oldItem.id == newItem.id
////            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: CardData, newItem: CardData): Boolean {
//            return oldItem.isShowAfterTraining == newItem.isShowAfterTraining
//        }
//
//        override fun getChangePayload(oldItem: CardData, newItem: CardData): Any? {
//            return if(oldItem.isShowAfterTraining != newItem.isShowAfterTraining) {
//                Log.i(TAG,"getChangePayload")
//                newItem.isShowAfterTraining
//            } else {
//                null
//            }
//        }
//    }
//
//    class VBViewHolder(var binding: ItemCardDetailBinding): QuickViewHolder(binding.root), LifecycleOwner {
//        val lifecycleRegistry = LifecycleRegistry(this).apply {
//            currentState = Lifecycle.State.CREATED
//        }
//        override val lifecycle: Lifecycle = lifecycleRegistry
//    }
//
//    override fun onBindViewHolder(
//        holder: CardDetailViewpagerAdapter.VBViewHolder,
//        position: Int,
//        item: CardData?
//    ) {
//
//    }
//
//    override fun onCreateViewHolder(
//        context: Context,
//        parent: ViewGroup,
//        viewType: Int
//    ): CardDetailViewpagerAdapter.VBViewHolder {
//        TODO("Not yet implemented")
//    }
//}