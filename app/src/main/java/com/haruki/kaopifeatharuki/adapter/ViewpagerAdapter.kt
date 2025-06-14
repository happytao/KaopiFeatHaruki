package com.haruki.kaopifeatharuki.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.haruki.kaopifeatharuki.fragment.CardListFragment
import com.haruki.kaopifeatharuki.util.ConstUtil
import java.lang.ref.WeakReference

class ViewpagerAdapter(private val fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragmentMap = SparseArray<WeakReference<CardListFragment>>()

    private val bandList = listOf(ConstUtil.BAND_ALL,
        ConstUtil.BAND_VIRTUAL_SINGER,
        ConstUtil.BAND_LEO_NEED,
        ConstUtil.BAND_MORE_MORE_JUMP,
        ConstUtil.BAND_VIVID_BAD_SQUAD,
        ConstUtil.BAND_WONDERLAND_SHOWTIME,
        ConstUtil.BAND_25_NIGHT_CORD)

    override fun getItemCount(): Int {
        return bandList.size
    }

    override fun createFragment(position: Int): Fragment {
        return CardListFragment.newInstance(bandList[position]).also {
            fragmentMap.put(position, WeakReference(it))
        }
    }

    fun getCurrentFragment(position: Int): CardListFragment? {
        return fragmentMap[position]?.get()
    }
}