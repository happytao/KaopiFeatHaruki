package com.haruki.kaopifeatharuki.repo.data.gacha


import com.google.gson.annotations.SerializedName
import com.haruki.kaopifeatharuki.util.ConstUtil.GACHA_BANNER_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.GACHA_BANNER_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.HARUKI_ASSET_URL_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.NORMAL_THUMBNAIL_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.THUMBNAIL_PREFIX
import com.haruki.kaopifeatharuki.util.glide.ImagePathUtil

data class GachaDetailsItem(@SerializedName("gachaId")
                            val gachaId: Int = 0,
                            @SerializedName("cardId")
                            val cardId: Int = 0,
                            @SerializedName("weight")
                            val weight: Int = 0,
                            @SerializedName("id")
                            val id: Int = 0,
                            @SerializedName("isWish")
                            val isWish: Boolean = false)


data class GachaData(@SerializedName("gachaType")
                     val gachaType: String = "",
                     @SerializedName("gachaInformation")
                     val gachaInformation: GachaInformation?,
                     @SerializedName("endAt")
                     val endAt: Long = 0,
                     @SerializedName("wishFixedSelectCount")
                     val wishFixedSelectCount: Int = 0,
                     @SerializedName("gachaCardRarityRates")
                     val gachaCardRarityRates: List<GachaCardRarityRatesItem>?,
                     @SerializedName("isShowPeriod")
                     val isShowPeriod: Boolean = false,
                     @SerializedName("dailySpinLimit")
                     val dailySpinLimit: Int = 0,
                     @SerializedName("wishLimitedSelectCount")
                     val wishLimitedSelectCount: Int = 0,
                     @SerializedName("assetbundleName")
                     val assetbundleName: String = "",
                     @SerializedName("gachaBehaviors")
                     val gachaBehaviors: List<GachaBehaviorsItem>?,
                     @SerializedName("name")
                     val name: String = "",
                     @SerializedName("gachaDetails")
                     val gachaDetails: List<GachaDetailsItem>?,
                     @SerializedName("id")
                     val id: Int = 0,
                     @SerializedName("gachaCeilItemId")
                     val gachaCeilItemId: Int = 0,
                     @SerializedName("wishSelectCount")
                     val wishSelectCount: Int = 0,
                     @SerializedName("seq")
                     val seq: Int = 0,
                     @SerializedName("startAt")
                     val startAt: Long = 0,
                     @SerializedName("gachaPickups")
                     val gachaPickups: List<GachaPickupsItem>?) {
    val gachaBannerUrl:String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return HARUKI_ASSET_URL_PREFIX + GACHA_BANNER_PREFIX + "banner_gacha$id/banner_gacha$id" + GACHA_BANNER_TAIL
        }

    val displayBannerUrl:String
        get() {
            val file = ImagePathUtil.getLocalFile(gachaBannerUrl)
            if(file != null) return file.absolutePath
            return gachaBannerUrl
        }
}


data class GachaPickupsItem(@SerializedName("gachaId")
                            val gachaId: Int = 0,
                            @SerializedName("cardId")
                            val cardId: Int = 0)


data class GachaInformation(@SerializedName("summary")
                            val summary: String = "",
                            @SerializedName("gachaId")
                            val gachaId: Int = 0,
                            @SerializedName("description")
                            val description: String = "")


data class GachaCardRarityRatesItem(@SerializedName("cardRarityType")
                                    val cardRarityType: String = "",
                                    @SerializedName("rate")
                                    val rate: Double = 0.0,
                                    @SerializedName("lotteryType")
                                    val lotteryType: String = "")


data class GachaBehaviorsItem(@SerializedName("costResourceQuantity")
                              val costResourceQuantity: Int = 0,
                              @SerializedName("spinCount")
                              val spinCount: Int = 0,
                              @SerializedName("gachaId")
                              val gachaId: Int = 0,
                              @SerializedName("executeLimit")
                              val executeLimit: Int = 0,
                              @SerializedName("groupId")
                              val groupId: Int = 0,
                              @SerializedName("id")
                              val id: Int = 0,
                              @SerializedName("costResourceType")
                              val costResourceType: String = "",
                              @SerializedName("priority")
                              val priority: Int = 0,
                              @SerializedName("gachaSpinnableType")
                              val gachaSpinnableType: String = "",
                              @SerializedName("gachaBehaviorType")
                              val gachaBehaviorType: String = "",
                              @SerializedName("resourceCategory")
                              val resourceCategory: String = "")


