package com.haruki.kaopifeatharuki.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import com.chad.library.adapter4.BaseDifferAdapter
import com.chad.library.adapter4.layoutmanager.QuickGridLayoutManager
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.haruki.kaopifeatharuki.R
import com.haruki.kaopifeatharuki.databinding.ItemCardDetailBinding
import com.haruki.kaopifeatharuki.repo.data.card.CardData
import com.haruki.kaopifeatharuki.repo.datamanager.CardSuppliesManager
import com.haruki.kaopifeatharuki.repo.datamanager.CharacterInfoManager
import com.haruki.kaopifeatharuki.util.TimeUtils
import com.haruki.kaopifeatharuki.util.dp
import com.haruki.kaopifeatharuki.util.imageviewer.showViewer
import com.haruki.kaopifeatharuki.util.observe
import com.haruki.kaopifeatharuki.util.postLoadImage
import com.haruki.kaopifeatharuki.util.postLoadResImage
import com.haruki.kaopifeatharuki.util.postText
import com.haruki.kaopifeatharuki.viewmodel.CardViewModel

class CardDetailViewpagerAdapter(private val mViewModel: CardViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val viewpager:ViewPager2)
    :BaseDifferAdapter<CardData, CardDetailViewpagerAdapter.VBViewHolder>(DiffCallback()) {
    companion object {
        private const val TAG = "CardDetailViewpagerAdapter"
    }

    private var isCardEpisodeFirstPartChecked = true
    private var isCardEpisodeSecondPartChecked = true
    private var currentMasterRank = 0
    private var currentSkillRank = 1
    private var currentCharacterRank = 1
    private var adapterLifecycleScope = lifecycleOwner.lifecycleScope
    private var isShowAfterTrainingSkill = false
    private val clothesAdapter:CardClothesAdapter by lazy {
        CardClothesAdapter()
    }
    private val lifecycleObserve = object: DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            viewpager.unregisterOnPageChangeCallback(viewpagerChangeCallback)
        }
    }

    private val viewpagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mViewModel.currentPosition = position
            viewpager.post {
                mViewModel.getCardPower()
                mViewModel.getSkillDescription()
                mViewModel.getCardClothes(mViewModel.currentCardList[position].id)
            }
            val lastPosition = items.size - 1
            if(position >= lastPosition - 3) {
                mViewModel.loadMore()
            }
            viewpager.offscreenPageLimit = OFFSCREEN_PAGE_LIMIT_DEFAULT

        }
    }


    init {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserve)
        viewpager.registerOnPageChangeCallback(viewpagerChangeCallback)
    }

    class DiffCallback: DiffUtil.ItemCallback<CardData>() {
        override fun areItemsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem.isShowAfterTraining == newItem.isShowAfterTraining
        }

        override fun getChangePayload(oldItem: CardData, newItem: CardData): Any? {
            return if(oldItem.isShowAfterTraining != newItem.isShowAfterTraining) {
                Log.i(TAG,"getChangePayload")
                newItem.isShowAfterTraining
            } else {
                null
            }
        }
    }

    class VBViewHolder(var binding: ItemCardDetailBinding): QuickViewHolder(binding.root), LifecycleOwner {
        val lifecycleRegistry = LifecycleRegistry(this).apply {
            currentState = Lifecycle.State.CREATED
        }
        override val lifecycle: Lifecycle = lifecycleRegistry
    }

    override fun onBindViewHolder(holder: VBViewHolder, position: Int, item: CardData?) {
        Log.i(TAG,"onBindViewHolder $position")
        if(item == null) return
        holder.binding.root.post {
            holder.lifecycleRegistry.currentState = Lifecycle.State.STARTED
            initListener(holder, position, item)
        }
        holder.binding.ivCardAttr.visibility = View.GONE
        holder.binding.ivDetailCardImg.postLoadImage(item.displaySmallImgUrl,
            loadCallback = { isLoadSuccess ->
                if(isLoadSuccess) {
                    showCardRarity(holder, item)
                    showAttrIcon(holder, item)
                }
            })
        showUnitIcon(holder, item)
        holder.binding.tvCardName.postText = item.prefix
        holder.binding.tvCardCharacterName.postText = CharacterInfoManager.getCharacterName(item.characterId)
        holder.binding.tvCardId.postText = item.id.toString()
        showCardSupplyType(holder, item)
        holder.binding.tvCardReleaseTime.postText = TimeUtils.timestampToTimeStr(item.releaseAt)
        holder.binding.tvSkillName.postText = item.cardSkillName
        if(item.specialTrainingSkillName != null) {
            if(!isShowAfterTrainingSkill) isShowAfterTrainingSkill = true
            setAfterTrainingLayoutVisibility(holder, true, item)
            holder.binding.tvAfterTrainingSkillName.postText = item.specialTrainingSkillName!!
        } else {
            if(isShowAfterTrainingSkill) isShowAfterTrainingSkill = false
            setAfterTrainingLayoutVisibility(holder, false, item)
        }
        showCardClothes(holder, item)


    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VBViewHolder {
        val startTime = System.currentTimeMillis()
        val view = ItemCardDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        Log.d(TAG,"view holder no cache inflate time: ${System.currentTimeMillis() - startTime}")
        return VBViewHolder(view)
    }


    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        clearStatus(holder as VBViewHolder)
        super.onViewRecycled(holder)
    }

    private fun initListener(holder: VBViewHolder, position: Int, item: CardData) {
        mViewModel.cardPower.observe(adapterLifecycleScope, holder.lifecycle) {
            Log.i(TAG,"cardPower: $it")
            holder.binding.tvPowerValue.text = it.toString()
        }

        Log.i(TAG,"start observe cardSkillDescription")
        mViewModel.cardSkillDescription.observe(adapterLifecycleScope, holder.lifecycle) {
            Log.i(TAG,"cardSkillDescription: $it")
            holder.binding.tvSkillDescription.text = it
        }

        mViewModel.cardSpecialSkillDescription.observe(adapterLifecycleScope, holder.lifecycle) {
            holder.binding.tvAfterTrainingSkillDescription.text = it
        }

        mViewModel.cardClothes.observe(adapterLifecycleScope, holder.lifecycle)  { clothesList ->
            clothesAdapter.submitList(clothesList)
        }

        holder.binding.ivDetailCardImg.setOnClickListener {
            holder.binding.ivDetailCardImg.showViewer(item.displayLargeImgUrl, item.displaySmallImgUrl)
        }

        holder.binding.chipCharaStoryBefore.setOnCheckedChangeListener { _, isChecked ->
            isCardEpisodeFirstPartChecked = isChecked
            mViewModel.getCardPower(isCardEpisodeFirstPartChecked, isCardEpisodeSecondPartChecked, currentMasterRank)
        }

        holder.binding.chipCharaStoryAfter.setOnCheckedChangeListener { _, isChecked ->
            isCardEpisodeSecondPartChecked = isChecked
            mViewModel.getCardPower(isCardEpisodeFirstPartChecked, isCardEpisodeSecondPartChecked, currentMasterRank)
        }

        holder.binding.slMasterRank.addOnChangeListener { _, value, _ ->
            currentMasterRank = value.toInt()
            mViewModel.getCardPower(isCardEpisodeFirstPartChecked, isCardEpisodeSecondPartChecked, currentMasterRank)

        }

        holder.binding.slSkillLevel.addOnChangeListener { _, value, _ ->
            currentSkillRank = value.toInt()
            mViewModel.getSkillDescription(currentSkillRank, currentCharacterRank)
        }

        holder.binding.slCharacterRank.addOnChangeListener { _, value, _ ->
            currentCharacterRank = value.toInt()
            mViewModel.getSkillDescription(currentSkillRank, currentCharacterRank)
        }
    }

    private fun clearStatus(holder: VBViewHolder) {
        holder.lifecycleRegistry.currentState = Lifecycle.State.CREATED
        holder.binding.ivDetailCardImg.setOnClickListener(null)
        holder.binding.chipCharaStoryBefore.apply {
            isChecked = true
            setOnCheckedChangeListener(null)
        }
        holder.binding.chipCharaStoryAfter.apply {
            isChecked = true
            setOnCheckedChangeListener(null)
        }
        holder.binding.slMasterRank.apply {
            value = 0f
            clearOnChangeListeners()
        }
        holder.binding.slSkillLevel.apply {
            value = 1f
            clearOnChangeListeners()
        }
        holder.binding.slCharacterRank.apply {
            value = 1f
            clearOnChangeListeners()
        }
        holder.binding.root.scrollY = 0

    }




    private fun showCardRarity(holder: VBViewHolder, item: CardData?) {
        when(item!!.cardRarityType) {
            "rarity_4" -> {
                if(item.isShowAfterTraining) {
                    holder.binding.ivCardRarity.postLoadResImage(R.drawable.rarity_star_4_after_training_vertical,18.dp.toInt(), 72.dp.toInt())
                } else{
                    holder.binding.ivCardRarity.postLoadResImage(R.drawable.rarity_star_4_normal_vertical,18.dp.toInt(), 72.dp.toInt())
                }
            }

            "rarity_3" -> {
                if(item.isShowAfterTraining) {
                    holder.binding.ivCardRarity.postLoadResImage(R.drawable.rarity_star_3_after_training_vertical,18.dp.toInt(), 54.dp.toInt())
                } else {
                    holder.binding.ivCardRarity.postLoadResImage(R.drawable.rarity_star_3_normal_vertical,18.dp.toInt(), 54.dp.toInt())
                }
            }
            "rarity_birthday" -> {
                holder.binding.ivCardRarity.postLoadResImage(R.drawable.rarity_birthday,18.dp.toInt(), 18.dp.toInt())
            }
            "rarity_2" -> {
                holder.binding.ivCardRarity.postLoadResImage(R.drawable.rarity_star_2_vertical,18.dp.toInt(), 36.dp.toInt())
            }
            "rarity_1" -> {
                holder.binding.ivCardRarity.postLoadResImage(R.drawable.rarity_star_normal,18.dp.toInt(), 18.dp.toInt())
            }
            else -> {}

        }
    }

    private fun showAttrIcon(holder: VBViewHolder, item: CardData?) {
        holder.binding.ivCardAttr.visibility = View.VISIBLE
        when(item!!.attr) {
            "pure" -> {
                holder.binding.ivCardAttr.postLoadResImage(R.mipmap.pure_icon)
                holder.binding.ivAttr.postLoadResImage(R.mipmap.pure_icon)
                holder.binding.tvAttrKey.postText = context.resources.getString(R.string.attr_pure)
            }
            "cute" -> {
                holder.binding.ivCardAttr.postLoadResImage(R.mipmap.cute_icon)
                holder.binding.ivAttr.postLoadResImage(R.mipmap.cute_icon)
                holder.binding.tvAttrKey.postText = context.resources.getString(R.string.attr_cute)
            }
            "mysterious"-> {
                holder.binding.ivCardAttr.postLoadResImage(R.mipmap.mysterious_icon)
                holder.binding.ivAttr.postLoadResImage(R.mipmap.mysterious_icon)
                holder.binding.tvAttrKey.postText = context.resources.getString(R.string.attr_mysterious)
            }
            "cool" -> {
                holder.binding.ivCardAttr.postLoadResImage(R.mipmap.cool_icon)
                holder.binding.ivAttr.postLoadResImage(R.mipmap.cool_icon)
                holder.binding.tvAttrKey.postText = context.resources.getString(R.string.attr_cool)
            }
            "happy" -> {
                holder.binding.ivCardAttr.postLoadResImage(R.mipmap.happy_icon)
                holder.binding.ivAttr.postLoadResImage(R.mipmap.happy_icon)
                holder.binding.tvAttrKey.postText = context.resources.getString(R.string.attr_happy)
            }
            else -> {}
        }

    }

    private fun showUnitIcon(holder: VBViewHolder, item: CardData?) {
        if(item == null) return
        var unit = item.supportUnit
        if(unit == "none") {
            unit = CharacterInfoManager.getCharacterUnit(item.characterId)
        }

        when(unit) {
            "light_sound" -> {
                holder.binding.ivBandIcon.postLoadResImage(R.mipmap.logo_light_sound)
            }
            "idol" -> {
                holder.binding.ivBandIcon.postLoadResImage(R.mipmap.logo_idol)

            }
            "street" -> {
                holder.binding.ivBandIcon.postLoadResImage(R.mipmap.logo_street)
            }
            "theme_park" -> {
                holder.binding.ivBandIcon.postLoadResImage(R.mipmap.logo_theme_park)
            }
            "school_refusal" -> {
                holder.binding.ivBandIcon.postLoadResImage(R.mipmap.logo_school_refusal)
            }
            "piapro" -> {
                holder.binding.ivBandIcon.postLoadResImage(R.mipmap.logo_piapro)
            }
            else -> {}
        }

    }


    private fun showCardSupplyType(holder: VBViewHolder, item: CardData?) {
        if(item == null) return
        var supplyId = item.cardSupplyId
        var cardSupplyType = CardSuppliesManager.getCardSupplyType(supplyId)
        when(cardSupplyType) {
            "normal" -> {
                holder.binding.tvLimitTypeValue.postText = context.resources.getString(R.string.card_supply_type_normal)
            }
            "birthday" -> {
                holder.binding.tvLimitTypeValue.postText = context.resources.getString(R.string.card_supply_type_birthday)
            }
            "term_limited" -> {
                holder.binding.tvLimitTypeValue.postText = context.resources.getString(R.string.card_supply_type_term_limited)
            }
            "colorful_festival_limited" -> {
                holder.binding.tvLimitTypeValue.postText = context.resources.getString(R.string.card_supply_type_c_fes)
            }
            "bloom_festival_limited" -> {
                holder.binding.tvLimitTypeValue.postText = context.resources.getString(R.string.card_supply_type_b_fes)
            }
            "unit_event_limited" -> {
                holder.binding.tvLimitTypeValue.postText = context.resources.getString(R.string.card_supply_type_unit_limited)
            }
            "collaboration_limited" -> {
                holder.binding.tvLimitTypeValue.postText = context.resources.getString(R.string.card_supply_type_collaboration_limited)
            }
            else -> {}
        }
    }

    private fun setAfterTrainingLayoutVisibility(holder: VBViewHolder, isVisible: Boolean, item: CardData) {
        val visibility = if(isVisible) View.VISIBLE else View.GONE
        holder.binding.tvAfterTraining.visibility = visibility
        holder.binding.tvAfterTrainingSkillNameKey.visibility = visibility
        holder.binding.tvAfterTrainingSkillName.visibility = visibility
        holder.binding.tvAfterTrainingSkillDescriptionKey.visibility = visibility
        holder.binding.tvAfterTrainingSkillDescription.visibility = visibility
        if(item.specialTrainingSkillId == 22) {
            holder.binding.slCharacterRank.visibility = View.VISIBLE
            holder.binding.tvCharacterRank.visibility = View.VISIBLE
        } else {
            holder.binding.slCharacterRank.visibility = View.GONE
            holder.binding.tvCharacterRank.visibility = View.GONE
        }

    }


    private fun showCardClothes(holder: VBViewHolder, item: CardData?) {
        holder.binding.rlClothes.layoutManager = QuickGridLayoutManager(context,4)
        holder.binding.rlClothes.adapter = clothesAdapter
    }








}