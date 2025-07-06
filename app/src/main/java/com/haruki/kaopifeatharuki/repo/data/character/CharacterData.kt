package com.haruki.kaopifeatharuki.repo.data.character


import com.google.gson.annotations.SerializedName

data class CharacterData(@SerializedName("firstName")
                         val firstName: String = "",
                         @SerializedName("resourceId")
                         val resourceId: Int = 0,
                         @SerializedName("figure")
                         val figure: String = "",
                         @SerializedName("unit")
                         val unit: String = "",
                         @SerializedName("gender")
                         val gender: String = "",
                         @SerializedName("givenName")
                         val givenName: String = "",
                         @SerializedName("id")
                         val id: Int = 0,
                         @SerializedName("live2dHeightAdjustment")
                         val liveDHeightAdjustment: Int = 0,
                         @SerializedName("height")
                         val height: Int = 0,
                         @SerializedName("breastSize")
                         val breastSize: String = "",
                         @SerializedName("supportUnitType")
                         val supportUnitType: String = "")


