package com.haruki.kaopifeatharuki.repo.data.clothes


import com.google.gson.annotations.SerializedName
import com.haruki.kaopifeatharuki.repo.database.clothes.ClothesDBData
import com.haruki.kaopifeatharuki.util.ConstUtil.CARD_CLOTHES_THUMBNAIL_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.CARD_CLOTHES_THUMBNAIL_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.HARUKI_ASSET_URL_PREFIX
import com.haruki.kaopifeatharuki.util.ConstUtil.NORMAL_THUMBNAIL_TAIL
import com.haruki.kaopifeatharuki.util.ConstUtil.THUMBNAIL_PREFIX
import com.haruki.kaopifeatharuki.util.glide.ImagePathUtil

data class ClothesData(@SerializedName("howToObtain")
                       val howToObtain: String? = "",
                       @SerializedName("colorName")
                       val colorName: String = "",
                       @SerializedName("publishedAt")
                       val publishedAt: Long = 0,
                       @SerializedName("archivePublishedAt")
                       val archivePublishedAt: Long = 0,
                       @SerializedName("colorId")
                       val colorId: Int = 0,
                       @SerializedName("costume3dType")
                       val costumeDType: String = "",
                       @SerializedName("designer")
                       val designer: String = "",
                       @SerializedName("costume3dGroupId")
                       val costumeDGroupId: Int = 0,
                       @SerializedName("assetbundleName")
                       val assetbundleName: String = "",
                       @SerializedName("costume3dRarity")
                       val costumeDRarity: String = "",
                       @SerializedName("name")
                       val name: String = "",
                       @SerializedName("archiveDisplayType")
                       val archiveDisplayType: String = "",
                       @SerializedName("id")
                       val id: Int = 0,
                       @SerializedName("characterId")
                       val characterId: Int = 0,
                       @SerializedName("partType")
                       val partType: String = "",
                       @SerializedName("seq")
                       val seq: Int = 0,
                       val cardId:Int? = 0) {

    constructor(clothesDBData: ClothesDBData) : this(
        howToObtain = clothesDBData.howToObtain,
        colorName = clothesDBData.colorName,
        publishedAt = clothesDBData.publishedAt,
        archivePublishedAt = clothesDBData.archivePublishedAt,
        colorId = clothesDBData.colorId,
        costumeDType = clothesDBData.costumeDType,
        designer = clothesDBData.designer,
        costumeDGroupId = clothesDBData.costumeDGroupId,
        assetbundleName = clothesDBData.assetbundleName,
        costumeDRarity = clothesDBData.costumeDRarity,
        name = clothesDBData.name,
        archiveDisplayType = clothesDBData.archiveDisplayType,
        id = clothesDBData.id,
        characterId = clothesDBData.characterId,
        partType = clothesDBData.partType,
        seq = clothesDBData.seq,
        cardId = clothesDBData.cardId
    )

    val clothesUrl: String
        get() {
            if(assetbundleName.isEmpty()) return ""
            return HARUKI_ASSET_URL_PREFIX + CARD_CLOTHES_THUMBNAIL_PREFIX + assetbundleName + CARD_CLOTHES_THUMBNAIL_TAIL
        }

    val displayClothesUrl:String
        get() {
            val file = ImagePathUtil.getLocalFile(clothesUrl)
            if(file != null) return file.absolutePath
            return clothesUrl
        }
}


