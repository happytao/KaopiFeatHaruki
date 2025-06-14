package com.haruki.kaopifeatharuki.fragment

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.adapter.CharacterChipAdapter
import com.haruki.kaopifeatharuki.databinding.CardFilterExpandableBinding
import com.haruki.kaopifeatharuki.repo.data.CharacterChip

class CardFilterBottomSheetFragment: BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CardFilterBottomSheetFragment"
    }

    private val mBinding:CardFilterExpandableBinding by lazy {
        CardFilterExpandableBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        CharacterChipAdapter()
    }

    private val iconFilterChipGroupList = mutableListOf<ChipGroup>()

    private val chipGroupListenersMap = mutableMapOf<ChipGroup, MutableList<(Chip, Boolean) -> Unit>>()


    private val characterChipList = listOf(
        CharacterChip("miku",R.mipmap.miku_icon),
        CharacterChip("rin",R.mipmap.rin_icon),
        CharacterChip("ren",R.mipmap.ren_icon),
        CharacterChip("luka",R.mipmap.luka_icon),
        CharacterChip("meiko",R.mipmap.meiko_icon),
        CharacterChip("kaito",R.mipmap.kaito_icon),
        CharacterChip("ichika",R.mipmap.ichika_icon),
        CharacterChip("saki",R.mipmap.saki_icon),
        CharacterChip("honami",R.mipmap.honami_icon),
        CharacterChip("shihou",R.mipmap.shihou_icon),
        CharacterChip("minori",R.mipmap.minori_icon),
        CharacterChip("haruka",R.mipmap.haruka_icon),
        CharacterChip("airi",R.mipmap.airi_icon),
        CharacterChip("shizuku",R.mipmap.shizuku_icon),
        CharacterChip("kohane",R.mipmap.kohane_icon),
        CharacterChip("an",R.mipmap.an_icon),
        CharacterChip("akito",R.mipmap.akito_icon),
        CharacterChip("touya",R.mipmap.touya_icon),
        CharacterChip("tsukasa",R.mipmap.tsukasa_icon),
        CharacterChip("emu",R.mipmap.emu_icon),
        CharacterChip("neinei",R.mipmap.neinei_icon),
        CharacterChip("rui",R.mipmap.rui_icon),
        CharacterChip("kanade",R.mipmap.kanade_icon),
        CharacterChip("mafuyu",R.mipmap.mafuyu_icon),
        CharacterChip("ena", R.mipmap.ena_icon),
        CharacterChip("mizuki", R.mipmap.mizuki_icon)
    )

    private val virtualSingerNames = listOf(
        "miku","rin","ren","luka","meiko","kaito"
    )

    private val leoNeedNames = listOf(
        "ichika","saki","honami","shihou"
    )
    private val mmjNames = listOf(
        "minori","haruka","airi","shizuku"
    )

    private val vbsNames = listOf(
        "kohane","an","akito","touya"
    )

    private val wsNames = listOf(
        "tsukasa","emu","neinei","rui"
    )

    private val night25Names = listOf(
        "kanade","mafuyu","ena","mizuki"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initListener()
    }


    private fun initData() {
        mBinding.rvFilterChara.adapter = adapter
        adapter.submitList(characterChipList)

        mBinding.rbSortDesc.isChecked = true
        mBinding.rbSortReleaseTime.isChecked = true
    }

    private fun initListener() {
        iconFilterChipGroupList.add(mBinding.chipIconBandGroup)
        iconFilterChipGroupList.add(mBinding.chipIconAttributeGroup)
        iconFilterChipGroupList.forEach { iconGroup ->
            iconGroup.addOnChipClickListener { clickedChip, isChecked ->
                if(!isChecked) {
                    clickedChip.chipIcon?.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                        setSaturation(0f)
                    })
                } else {
                    clickedChip.chipIcon?.colorFilter = null
                }
            }
        }



        mBinding.chipIconBandGroup.addOnChipClickListener { chip, isChecked ->
            val newList = adapter.items.map {
                it.copy()
            }
            when(chip.id) {
                R.id.chip_virtual_singer -> {
                    newList.forEach {
                        if(virtualSingerNames.contains(it.name)) {
                            it.isChecked = isChecked
                        }
                    }
                }

                R.id.chip_leoneed -> {
                    newList.forEach {
                        if(leoNeedNames.contains(it.name)) {
                            it.isChecked = isChecked
                        }
                    }
                }

                R.id.chip_mmj -> {
                    newList.forEach {
                        if(mmjNames.contains(it.name)) {
                            it.isChecked = isChecked
                        }
                    }
                }

                R.id.chip_vbs -> {
                    newList.forEach {
                        if(vbsNames.contains(it.name)) {
                            it.isChecked = isChecked
                        }
                    }
                }

                R.id.chip_ws -> {
                    newList.forEach {
                        if(wsNames.contains(it.name)) {
                            it.isChecked = isChecked
                        }
                    }
                }

                R.id.chip_25_night -> {
                    newList.forEach {
                        if(night25Names.contains(it.name)) {
                            it.isChecked = isChecked
                        }
                    }
                }
                else -> {}

            }


            adapter.submitList(newList)
        }

        adapter.setOnItemClickListener { _,view,pos ->
            val chip = view as? Chip
            val isChecked = chip?.isChecked ?: false
            if(isChecked) {
                chip?.chipIcon?.colorFilter = null
            } else {
                chip?.chipIcon?.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
                    setSaturation(0f)
                })

            }
        }
    }


    fun ChipGroup.addOnChipClickListener(listener: (Chip, Boolean) -> Unit) {
        // 获取或创建当前ChipGroup的监听器列表
        val listeners = chipGroupListenersMap.getOrPut(this) { mutableListOf() }

        if (listeners.isEmpty()) {
            // 第一次为这个ChipGroup添加监听时，设置所有Chip的点击事件
            for (i in 0 until childCount) {
                val chip = (getChildAt(i) as? Chip)
                chip?.setOnClickListener {
                    val isChecked = chip.isChecked
                    // 只触发当前ChipGroup的监听器
                    chipGroupListenersMap[this]?.forEach { callback ->
                        callback(chip, isChecked)
                    }
                }
            }
        }

        // 将监听器添加到当前ChipGroup的监听器列表中
        listeners.add(listener)
    }



}