package com.haruki.kaopifeatharuki.repo.data.card


import com.google.gson.annotations.SerializedName

data class CardSupply(@SerializedName("assetbundleName")
                      val assetbundleName: String = "",
                      @SerializedName("cardSupplyType")
                      val cardSupplyType: String = "",
                      @SerializedName("id")
                      val id: Int = 0)


