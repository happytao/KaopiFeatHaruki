package com.haruki.kaopifeatharuki.repo.data

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.haruki.kaopifeatharuki.repo.database.CardDBData
import com.haruki.kaopifeatharuki.util.ConstUtil.AFTER_TRAINING_THUMBNAIL_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.HARUKI_ASSET_URL_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.NORMAL_THUMBNAIL_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.SEKAI_VIEWER_ASSET_URL_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.THUMBNAIL_PREFIX

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
                    val cardParameters: CardParameters? = null) {

    val normalThumbnailUrl: String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return HARUKI_ASSET_URL_PREFIX + THUMBNAIL_PREFIX + assetbundleName + NORMAL_THUMBNAIL_TAIL
        }

    val afterTrainingThumbnailUrl: String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return HARUKI_ASSET_URL_PREFIX + THUMBNAIL_PREFIX + assetbundleName + AFTER_TRAINING_THUMBNAIL_TAIL
        }

    var isShowAfterTraining: Boolean = true

    constructor(cardDBData: CardDBData):this(
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
        seq = cardDBData.seq)

}