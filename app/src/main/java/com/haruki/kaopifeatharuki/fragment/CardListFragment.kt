package com.haruki.kaopifeatharuki.fragment


import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.layoutmanager.QuickGridLayoutManager
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter.OnTrailingListener
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.activity.MainActivity
import com.haruki.kaopifeatharuki.adapter.CardListAdapter
import com.haruki.kaopifeatharuki.adapter.CardListLoadMoreAdapter
import com.haruki.kaopifeatharuki.base.BaseFragment
import com.haruki.kaopifeatharuki.databinding.FragmentCardListBinding
import com.haruki.kaopifeatharuki.navigation.CardDetail
import com.haruki.kaopifeatharuki.repo.data.CardData
import com.haruki.kaopifeatharuki.util.ConstUtil.BAND_ALL
import com.haruki.kaopifeatharuki.util.ToastUtil
import com.haruki.kaopifeatharuki.util.observe
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel



class CardListFragment: BaseFragment<FragmentCardListBinding, CardViewModel>() {
    override val mViewModel: CardViewModel by viewModels({requireActivity()})

    private var band: String?= null

    private val adapter: CardListAdapter by lazy {
        CardListAdapter()
    }
    private var adapterHelper:QuickAdapterHelper? = null

//    private var cardListCurrentPageIndex = 0


    private var isAfterTraining = true
    private val screenHeight by lazy {
        resources.displayMetrics.heightPixels
    }


    companion object{
        private const val TAG = "CardListFragment"
    }




    override fun getLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCardListBinding {
        return FragmentCardListBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        val layoutManager = QuickGridLayoutManager(requireContext(),3)
        mBinding.recyclerView.layoutManager = layoutManager
        setCardListHeaderAndTrailingLoad()
        editClearFocus()
    }

    override fun onDestroyView() {
        mViewModel.currentPosition = (mBinding.recyclerView.layoutManager as QuickGridLayoutManager)
            .findFirstVisibleItemPosition()
        Log.i(TAG, "currentPosition : ${mViewModel.currentPosition}")
        mViewModel.isDataLoaded = true
        super.onDestroyView()
    }


    override fun initData() {
        mViewModel.cardList.observe(this) { cardList ->
            if(cardList.isEmpty()) {
                adapterHelper?.trailingLoadState = LoadState.NotLoading(true)
                ToastUtil.showToast(requireContext(), "没有更多数据")
            } else {
                adapterHelper?.trailingLoadState = LoadState.NotLoading(false)
            }
            if(mViewModel.cardListCurrentPageIndex == 0) {
                adapter.submitList(cardList)
            } else {
                adapter.addAll(cardList)
            }

        }

        mViewModel.changeTrainingStateCardList.observe(this) { cardList ->
            Log.i(TAG,"changeTrainingStateCardList ${cardList.size}")
            adapter.submitList(cardList)

        }

        mViewModel.cardDataById.observe(this) { cardData ->
            Log.i(TAG,"cardDataById ${cardData?.id}")
            mViewModel.currentPosition = (mBinding.recyclerView.layoutManager as QuickGridLayoutManager)
                .findFirstVisibleItemPosition()
            adapterHelper?.trailingLoadState = LoadState.NotLoading(true)
            val newList = cardData?.let { listOf(it) }?: listOf()
            adapter.submitList(newList)
        }

        mViewModel.restoreEvent.observe(this) { cardList ->
            Log.i(TAG,"restoreEvent, list size: ${cardList.size} pos:${mViewModel.currentPosition}")
            adapter.submitList(cardList) {
                mBinding.recyclerView.scrollToPosition(mViewModel.currentPosition)
            }
            adapterHelper?.trailingLoadState = LoadState.NotLoading(false)


        }

        mBinding.btnFloating.setOnClickListener {
            mViewModel.changeTrainingState(adapter.items)
            if(mViewModel.isShowAfterTraining) {
                ToastUtil.showToast(requireContext(),"已切换为花后图")
            } else {
                ToastUtil.showToast(requireContext(),"已切换为花前图")
            }
        }

        mBinding.btnFloatingToTop.visibility = View.GONE

        mBinding.btnFloatingToTop.setOnClickListener {
            mBinding.recyclerView.scrollToPosition(0)
            mBinding.btnFloatingToTop.visibility = View.GONE
        }

        mBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = recyclerView.computeVerticalScrollOffset()
                mBinding.btnFloatingToTop.visibility =
                    if (offset > screenHeight) View.VISIBLE else View.GONE
            }
        })

        adapter.setOnItemClickListener{ _,_,pos ->
            mViewModel.selectPosition = pos
            Log.i(TAG,"setOnItemClickListener $pos")
            findNavController().navigate(CardDetail)
        }

        mBinding.btnFilter.setOnClickListener {
            val bottomSheetFragment = CardFilterBottomSheetFragment()
            bottomSheetFragment.show(childFragmentManager, CardFilterBottomSheetFragment.TAG)
        }

        mBinding.searchInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                Log.i(TAG,"searchInput complete")
                val idStr = textView.text
                if(idStr.isBlank()) {
                    mViewModel.restoreCardList()
                    mBinding.searchContainer.clearFocus()
                    hideKeyboard(mBinding.searchContainer)
                    return@setOnEditorActionListener false
                }
                try {
                    val id = idStr.toString().toInt()
                    mViewModel.loadCardById(id)
                } catch (e: Exception) {
                    Log.e(TAG,"parse id error")
                    Log.e(TAG, Log.getStackTraceString(e))
                    ToastUtil.showToast(requireContext(), "目前只支持输入id搜索")
                }


            }
            mBinding.searchContainer.clearFocus()
            hideKeyboard(mBinding.searchContainer)
            false
        }

        mBinding.searchContainer.setEndIconOnClickListener {
            mBinding.searchInput.text?.clear()
            mViewModel.restoreCardList()
        }

        if(!mViewModel.isDataLoaded) {
            mViewModel.loadCardList(10,mViewModel.cardListCurrentPageIndex)
        }
    }

    override fun restoreData() {
        Log.i(TAG,"restore fragment")
        mBinding.root.post {
            mViewModel.restoreCardList()
        }

    }

    private fun setCardListHeaderAndTrailingLoad() {
        val loadMoreAdapter = CardListLoadMoreAdapter()
        loadMoreAdapter.setOnLoadMoreListener(object: OnTrailingListener{
            override fun onFailRetry() {

            }

            override fun onLoad() {
                Log.i(TAG, "load more onLoad")
                mViewModel.cardListCurrentPageIndex += 1
                if(mViewModel.isFilterMode) {
                    mViewModel.loadCardByAllFilterParam(10,mViewModel.cardListCurrentPageIndex)
                } else {
                    mViewModel.loadCardList(10,mViewModel.cardListCurrentPageIndex)
                }

            }

        })
        adapterHelper = QuickAdapterHelper.Builder(adapter)
            .setTrailingLoadStateAdapter(loadMoreAdapter).build()
        mBinding.recyclerView.adapter = adapterHelper?.adapter



    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            Log.i(TAG,"onHiddenChanged hidden:$hidden")
            val newList = mViewModel.currentCardList.map {
                it.copy()
            }
            adapter.submitList(newList){
                mBinding.recyclerView.scrollToPosition(mViewModel.currentPosition)
            }

        }
    }

    /**
     * 设置当点击搜索框外关闭输入法和取消焦点
     */
    private fun editClearFocus() {
        (requireActivity() as MainActivity).setDispatchTouchEvent { event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // 判断触摸点是否在输入框外部
                val touchArea = Rect().apply { mBinding.searchContainer.getGlobalVisibleRect(this) }
                if (!touchArea.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    mBinding.searchContainer.clearFocus() // 清除焦点
                    hideKeyboard(mBinding.searchContainer) // 隐藏键盘
                }
            }
        }

    }

    private fun hideKeyboard(view: View) {
        val imm = requireActivity().getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}