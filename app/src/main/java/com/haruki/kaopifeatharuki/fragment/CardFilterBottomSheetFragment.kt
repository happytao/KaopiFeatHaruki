package com.haruki.kaopifeatharuki.fragment

import android.content.DialogInterface
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.adapter.CharacterChipAdapter
import com.haruki.kaopifeatharuki.databinding.CardFilterExpandableBinding
import com.haruki.kaopifeatharuki.repo.data.CardFilterParam
import com.haruki.kaopifeatharuki.repo.data.CharacterChip
import com.haruki.kaopifeatharuki.util.ConstUtil
import com.haruki.kaopifeatharuki.util.ConstUtil.ATTR_COOL
import com.haruki.kaopifeatharuki.util.ConstUtil.ATTR_CUTE
import com.haruki.kaopifeatharuki.util.ConstUtil.ATTR_HAPPY
import com.haruki.kaopifeatharuki.util.ConstUtil.ATTR_MYSTERIOUS
import com.haruki.kaopifeatharuki.util.ConstUtil.ATTR_PURE
import com.haruki.kaopifeatharuki.util.ConstUtil.RARITY_1
import com.haruki.kaopifeatharuki.util.ConstUtil.RARITY_2
import com.haruki.kaopifeatharuki.util.ConstUtil.RARITY_3
import com.haruki.kaopifeatharuki.util.ConstUtil.RARITY_4
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_CHECK_BONUS
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_HP_BONUS
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS_WHEN_BAND
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS_WHEN_HIGH_HP
import com.haruki.kaopifeatharuki.util.ConstUtil.SKILL_TYPE_POINT_BONUS_WHEN_PERFECT
import com.haruki.kaopifeatharuki.util.name2Id
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel

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

    private val mViewModel by viewModels<CardViewModel>({requireParentFragment()})

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
        restoreParam()
    }

    override fun onDismiss(dialog: DialogInterface) {
        mViewModel.filterParam = getCurrentFilterParam()
        super.onDismiss(dialog)
    }

    private fun initData() {
        mBinding.rvFilterChara.adapter = adapter
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
        //这俩货图标不用变灰，后面再添加
        iconFilterChipGroupList.add(mBinding.chipIconSkillGroup)
        iconFilterChipGroupList.add(mBinding.chipIconRarityGroup)




        mBinding.chipIconBandGroup.addOnChipClickListener { chip, isChecked ->
            // 深拷贝列表（确保每个 item 都是新的实例）
            val newList = adapter.items.map { it.copy() }

            // 定义 chipId 和对应 nameList 的映射关系
            val chipToNamesMap = mapOf(
                R.id.chip_virtual_singer to virtualSingerNames,
                R.id.chip_leoneed to leoNeedNames,
                R.id.chip_mmj to mmjNames,
                R.id.chip_vbs to vbsNames,
                R.id.chip_ws to wsNames,
                R.id.chip_25_night to night25Names
            )

            // 根据 chip.id 获取对应的 nameList，并更新 newList
            chipToNamesMap[chip.id]?.let { nameList ->
                newList.filter { it.name in nameList }
                    .forEach { it.isChecked = isChecked }
            }

            // 提交新列表
            adapter.submitList(newList)
        }

        mBinding.btnReset.setOnClickListener {
            mBinding.rgSortType.check(R.id.rb_sort_desc)
            mBinding.rgSortParam.check(R.id.rb_sort_release_time)
            iconFilterChipGroupList.forEach { iconGroup ->
                iconGroup.forEach { it ->
                    val chip = it as Chip
                    chip.isChecked = true
                }
            }
            val newList = adapter.items.map { it.copy() }
            newList.forEach { it.isChecked = true }
            adapter.submitList(newList)
        }

        mBinding.btnClear.setOnClickListener {
            iconFilterChipGroupList.forEach { iconGroup ->
                iconGroup.forEach { it ->
                    val chip = it as Chip
                    chip.isChecked = false
                }
            }
            val newList = adapter.items.map { it.copy() }
            newList.forEach { it.isChecked = false }
            adapter.submitList(newList)
        }

        mBinding.btnConfirm.setOnClickListener {
            mViewModel.filterParam = getCurrentFilterParam()
            mViewModel.cardListCurrentPageIndex = 0
            if(mViewModel.filterParam!!.isInitState()) {
                Log.i(TAG,"isInitState")
                mViewModel.loadCardList(10,mViewModel.cardListCurrentPageIndex)
            } else {
                mViewModel.loadCardByAllFilterParam(10,mViewModel.cardListCurrentPageIndex)
            }
            dismiss()
        }

        adapter.setOnItemClickListener { _,view,pos ->
            val chip = view as Chip
            val newList = adapter.items.map { it.copy() }
            newList[pos].isChecked = chip.isChecked
            adapter.submitList(newList)

        }
    }

    private fun getCurrentFilterParam():CardFilterParam {
        val isSortDec = mBinding.rbSortDesc.isChecked
        val sortParam = when(mBinding.rgSortParam.checkedRadioButtonId) {
            R.id.rb_sort_release_time -> "release_time"
            R.id.rb_sort_rarity -> "rarity"
            R.id.rb_sort_id -> "id"
            R.id.rb_sort_power -> "power"
            else -> ""

        }
        Log.i(TAG,"save isSortDec:$isSortDec sortParam:$sortParam")
        val bandList = mutableListOf<String>()
        val chipToBandName = mapOf(
            R.id.chip_virtual_singer to ConstUtil.BAND_VIRTUAL_SINGER,
            R.id.chip_leoneed to ConstUtil.BAND_LEO_NEED,
            R.id.chip_mmj to ConstUtil.BAND_MORE_MORE_JUMP,
            R.id.chip_vbs to ConstUtil.BAND_VIVID_BAD_SQUAD,
            R.id.chip_ws to ConstUtil.BAND_WONDERLAND_SHOWTIME,
            R.id.chip_25_night to ConstUtil.BAND_25_NIGHT_CORD
        )
        mBinding.chipIconBandGroup.forEach {
            val chip = it as Chip
            chipToBandName[chip.id]?.let { name ->
                if(chip.isChecked) bandList.add(name)
            }
        }
        val characterIdList = mutableListOf<Int>()
        adapter.items.forEach {
            if(it.isChecked) {
             characterIdList.add(it.name.name2Id)
            }
        }
        val attrList = mutableListOf<String>()
        val chipToAttrName = mapOf(
            R.id.chip_pure to ATTR_PURE,
            R.id.chip_mysterious to ATTR_MYSTERIOUS,
            R.id.chip_happy to ATTR_HAPPY,
            R.id.chip_cute to ATTR_CUTE,
            R.id.chip_cool to ATTR_COOL
        )
        mBinding.chipIconAttributeGroup.forEach {
            val chip = it as Chip
            chipToAttrName[chip.id]?.let { name ->
                if (chip.isChecked) attrList.add(name)
            }
        }
        val skillTypeList = mutableListOf<String>()
        val chipToSkillTypeName = mapOf(
            R.id.chip_skill_point_bonus to SKILL_TYPE_POINT_BONUS,
            R.id.chip_skill_hp_bonus to SKILL_TYPE_HP_BONUS,
            R.id.chip_skill_check_bonus to SKILL_TYPE_CHECK_BONUS,
            R.id.chip_skill_point_bonus_when_perfect to SKILL_TYPE_POINT_BONUS_WHEN_PERFECT,
            R.id.chip_skill_point_bonus_when_high_hp to SKILL_TYPE_POINT_BONUS_WHEN_HIGH_HP,
            R.id.chip_skill_point_bonus_when_band to SKILL_TYPE_POINT_BONUS_WHEN_BAND
        )
        mBinding.chipIconSkillGroup.forEach {
            val chip = it as Chip
            chipToSkillTypeName[chip.id]?.let { name ->
                if (chip.isChecked) skillTypeList.add(name)
            }
        }

        val rarityList = mutableListOf<String>()
        val chipToRarity = mapOf(
            R.id.chip_rarity_1 to RARITY_1,
            R.id.chip_rarity_2 to RARITY_2,
            R.id.chip_rarity_3 to RARITY_3,
            R.id.chip_rarity_4 to RARITY_4,
            R.id.chip_rarity_birthday to ConstUtil.RARITY_BIRTHDAY
        )
        mBinding.chipIconRarityGroup.forEach {
            val chip = it as Chip
            chipToRarity[chip.id]?.let { name ->
                if (chip.isChecked) rarityList.add(name)
            }
        }

        return CardFilterParam(
            isDescSort = isSortDec,
            sortedProperty = sortParam,
            filterBands = bandList,
            filterCharacterIds = characterIdList,
            filterAttrs = attrList,
            filterSkillTypes = skillTypeList,
            filterRarities = rarityList)

    }

    private fun restoreParam() {
        if(mViewModel.filterParam == null) {
            adapter.submitList(characterChipList)
            return
        }
        val filterParam = mViewModel.filterParam!!
        Log.i(TAG,"restore isSortDec:${filterParam.isDescSort} sortParam:${filterParam.sortedProperty}")
        if(filterParam.isDescSort) {
            mBinding.rgSortType.check(R.id.rb_sort_desc)
        } else {
            mBinding.rgSortType.check(R.id.rb_sort_asc)
        }
        when(filterParam.sortedProperty) {
            "release_time" -> mBinding.rgSortParam.check(R.id.rb_sort_release_time)
            "rarity" -> mBinding.rgSortParam.check(R.id.rb_sort_rarity)
            "id" -> mBinding.rgSortParam.check(R.id.rb_sort_id)
            "power" -> mBinding.rgSortParam.check(R.id.rb_sort_power)
            else -> {}
        }
        restoreChips(mBinding.chipIconBandGroup, filterParam.filterBands, mapOf(
                R.id.chip_virtual_singer to ConstUtil.BAND_VIRTUAL_SINGER,
        R.id.chip_leoneed to ConstUtil.BAND_LEO_NEED,
        R.id.chip_mmj to ConstUtil.BAND_MORE_MORE_JUMP,
        R.id.chip_vbs to ConstUtil.BAND_VIVID_BAD_SQUAD,
        R.id.chip_ws to ConstUtil.BAND_WONDERLAND_SHOWTIME,
        R.id.chip_25_night to ConstUtil.BAND_25_NIGHT_CORD
        ))
        restoreChips(mBinding.chipIconAttributeGroup, filterParam.filterAttrs, mapOf(
            R.id.chip_pure to ATTR_PURE,
            R.id.chip_mysterious to ATTR_MYSTERIOUS,
            R.id.chip_happy to ATTR_HAPPY,
            R.id.chip_cute to ATTR_CUTE,
            R.id.chip_cool to ATTR_COOL
        ))
        restoreChips(mBinding.chipIconSkillGroup, filterParam.filterSkillTypes, mapOf(
            R.id.chip_skill_point_bonus to SKILL_TYPE_POINT_BONUS,
            R.id.chip_skill_hp_bonus to SKILL_TYPE_HP_BONUS,
            R.id.chip_skill_check_bonus to SKILL_TYPE_CHECK_BONUS,
            R.id.chip_skill_point_bonus_when_perfect to SKILL_TYPE_POINT_BONUS_WHEN_PERFECT,
            R.id.chip_skill_point_bonus_when_high_hp to SKILL_TYPE_POINT_BONUS_WHEN_HIGH_HP,
            R.id.chip_skill_point_bonus_when_band to SKILL_TYPE_POINT_BONUS_WHEN_BAND

        ))
        restoreChips(mBinding.chipIconRarityGroup, filterParam.filterRarities, mapOf(
            R.id.chip_rarity_1 to RARITY_1,
            R.id.chip_rarity_2 to RARITY_2,
            R.id.chip_rarity_3 to RARITY_3,
            R.id.chip_rarity_4 to RARITY_4,
            R.id.chip_rarity_birthday to ConstUtil.RARITY_BIRTHDAY
        ))

         characterChipList.forEach {
            if(it.name.name2Id in filterParam.filterCharacterIds) {
                it.isChecked = true
            } else {
                it.isChecked = false
            }
        }

        val newList = characterChipList.map{
            val isChecked = it.name.name2Id in filterParam.filterCharacterIds
            it.copy(isChecked = isChecked)
        }
        adapter.submitList(newList)



    }

    fun restoreChips(chipGroup: ChipGroup, values: List<String>, mapping: Map<Int, String>) {
        chipGroup.forEach { view ->
            (view as Chip).isChecked = mapping[view.id] in values
        }
    }


    fun ChipGroup.addOnChipClickListener(listener: (Chip, Boolean) -> Unit) {
        // 获取或创建当前ChipGroup的监听器列表
        val listeners = chipGroupListenersMap.getOrPut(this) { mutableListOf() }

        if (listeners.isEmpty()) {
            // 第一次为这个ChipGroup添加监听时，设置所有Chip的点击事件
            for (i in 0 until childCount) {
                val chip = (getChildAt(i) as? Chip)
                chip?.setOnCheckedChangeListener { compoundButton, b ->
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