package com.haruki.kaopifeatharuki.repo.data.card

import com.google.gson.annotations.SerializedName
import com.haruki.kaopifeatharuki.repo.database.card.CardDBData
import com.haruki.kaopifeatharuki.util.ConstUtil.AFTER_TRAINING_CARD_IMG_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.AFTER_TRAINING_THUMBNAIL_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.CARD_LARGE_IMG_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.CARD_SMALL_IMG_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.HARUKI_ASSET_URL_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.NORMAL_CARD_IMG_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.NORMAL_THUMBNAIL_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.THUMBNAIL_PREFIX
import com.haruki.kaopifeatharuki.util.glide.ImagePathUtil

data class CardData(@SerializedName("specialTrainingPower3BonusFixed")
                    val specialTrainingPower3BonusFixed: Int = 0,
                    @SerializedName("masterLessonAchieveResources")
                    val masterLessonAchieveResources: List<MasterLessonAchieveResourcesItem>? = null,
                    //卡名
                    @SerializedName("prefix")
                    val prefix: String = "",
                    @SerializedName("archivePublishedAt")
                    val archivePublishedAt: Long = 0,
                    //抽卡语音
                    @SerializedName("gachaPhrase")
                    val gachaPhrase: String = "",
                    @SerializedName("cardSkillName")
                    val cardSkillName: String = "",
                    @SerializedName("specialTrainingCosts")
                    val specialTrainingCosts: List<SpecialTrainingCostsItem>? = null,
                    //开放时间时间戳
                    @SerializedName("releaseAt")
                    val releaseAt: Long = 0,
                    @SerializedName("skillId")
                    val skillId: Int = 0,
                    //图片资源链接名
                    @SerializedName("assetbundleName")
                    val assetbundleName: String = "",
                    //稀有度
                    @SerializedName("cardRarityType")
                    val cardRarityType: String = "",
                    @SerializedName("archiveDisplayType")
                    val archiveDisplayType: String = "",
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("specialTrainingPower2BonusFixed")
                    val specialTrainingPower2BonusFixed: Int = 0,
                    @SerializedName("supportUnit")
                    val supportUnit: String = "",
                    //属性
                    @SerializedName("attr")
                    val attr: String = "",
                    @SerializedName("characterId")
                    val characterId: Int = 0,
                    @SerializedName("specialTrainingPower1BonusFixed")
                    val specialTrainingPower1BonusFixed: Int = 0,
                    @SerializedName("seq")
                    val seq: Int = 0,
                    @SerializedName("cardParameters")
                    val cardParameters: CardParameters? = null,
                    @SerializedName("cardSupplyId")
                    var cardSupplyId: Int,
                    @SerializedName("specialTrainingSkillId")
                    var specialTrainingSkillId: Int?,
                    @SerializedName("specialTrainingSkillName")
                    var specialTrainingSkillName: String?,
                    val skillType:String = "",
                    val basePower:Int = 0,
                    var isShowAfterTraining: Boolean = true) {

    val normalThumbnailUrl: String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return HARUKI_ASSET_URL_PREFIX + THUMBNAIL_PREFIX + assetbundleName + NORMAL_THUMBNAIL_TAIL
        }

    val afterTrainingThumbnailUrl: String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return if(cardRarityType != "rarity_4" && cardRarityType != "rarity_3")
                normalThumbnailUrl
            else
                HARUKI_ASSET_URL_PREFIX + THUMBNAIL_PREFIX + assetbundleName + AFTER_TRAINING_THUMBNAIL_TAIL
        }

    val normalCardSmallImgUrl:String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return HARUKI_ASSET_URL_PREFIX + CARD_SMALL_IMG_PREFIX + assetbundleName + NORMAL_CARD_IMG_TAIL
        }

    val afterTrainingCardSmallImgUrl:String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return if(cardRarityType != "rarity_4" && cardRarityType != "rarity_3")
                normalCardLargeImgUrl
            else
                HARUKI_ASSET_URL_PREFIX + CARD_SMALL_IMG_PREFIX + assetbundleName + AFTER_TRAINING_CARD_IMG_TAIL

        }

    val normalCardLargeImgUrl:String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return HARUKI_ASSET_URL_PREFIX + CARD_LARGE_IMG_PREFIX + assetbundleName + NORMAL_CARD_IMG_TAIL
        }

    val afterTrainingCardLargeImgUrl:String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return if(cardRarityType != "rarity_4" && cardRarityType != "rarity_3")
                normalCardLargeImgUrl
            else
                HARUKI_ASSET_URL_PREFIX + CARD_LARGE_IMG_PREFIX + assetbundleName + AFTER_TRAINING_CARD_IMG_TAIL

        }

    val displayThumbnailUrl:String
        get() {
            if(isShowAfterTraining) {
                val file = ImagePathUtil.getLocalFile(afterTrainingThumbnailUrl)
                if(file != null) return file.absolutePath
                return afterTrainingThumbnailUrl
            } else {
                val file = ImagePathUtil.getLocalFile(normalThumbnailUrl)
                if(file != null) return file.absolutePath
                return normalThumbnailUrl
            }
        }

    val displaySmallImgUrl:String
        get() {
            if(isShowAfterTraining) {
                val file = ImagePathUtil.getLocalFile(afterTrainingCardSmallImgUrl)
                if(file != null) return file.absolutePath
                return afterTrainingCardSmallImgUrl
            } else {
                val file = ImagePathUtil.getLocalFile(normalCardSmallImgUrl)
                if(file != null) return file.absolutePath
                return normalCardSmallImgUrl
            }
        }

    val displayLargeImgUrl:String
        get() {
            if(isShowAfterTraining) {
                val file = ImagePathUtil.getLocalFile(afterTrainingCardLargeImgUrl)
                if(file != null) return file.absolutePath
                return afterTrainingCardLargeImgUrl
            } else {
                val file = ImagePathUtil.getLocalFile(normalCardLargeImgUrl)
                if(file != null) return file.absolutePath
                return normalCardLargeImgUrl
            }
        }


    constructor(cardDBData: CardDBData, isShowAfterTraining: Boolean = true,
                skillType: String = "", basePower: Int = 0):this(
        id = cardDBData.id,
        prefix = cardDBData.prefix,
        gachaPhrase = cardDBData.gachaPhrase,
        cardSkillName = cardDBData.cardSkillName,
        releaseAt = cardDBData.releaseAt,
        skillId = cardDBData.skillId,
        assetbundleName = cardDBData.assetbundleName,
        cardRarityType = cardDBData.cardRarityType,
        attr = cardDBData.attr,
        characterId = cardDBData.characterId,
        seq = cardDBData.seq,
        supportUnit = cardDBData.supportUnit,
        cardSupplyId = cardDBData.cardSupplyId,
        specialTrainingSkillId = cardDBData.specialTrainingSkillId,
        specialTrainingSkillName = cardDBData.specialTrainingSkillName,
        specialTrainingPower1BonusFixed = cardDBData.specialTrainingPower1BonusFixed,
        specialTrainingPower2BonusFixed = cardDBData.specialTrainingPower2BonusFixed,
        specialTrainingPower3BonusFixed = cardDBData.specialTrainingPower3BonusFixed,
        skillType = skillType,
        basePower = cardDBData.basePower,
        isShowAfterTraining = isShowAfterTraining) {
    }

}