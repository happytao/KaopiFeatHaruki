package com.haruki.kaopifeatharuki.repo.data.card


import com.google.gson.annotations.SerializedName

data class CostsItem(@SerializedName("resourceId")
                     val resourceId: Int = 0,
                     @SerializedName("quantity")
                     val quantity: Int = 0,
                     @SerializedName("resourceType")
                     val resourceType: String = "")


data class CardEpisode(@SerializedName("power2BonusFixed")
                       val power2BonusFixed: Int = 0,
                       @SerializedName("costs")
                       val costs: List<CostsItem>?,
                       @SerializedName("releaseConditionId")
                       val releaseConditionId: Int = 0,
                       @SerializedName("cardId")
                       val cardId: Int = 0,
                       @SerializedName("id")
                       val id: Int = 0,
                       @SerializedName("power3BonusFixed")
                       val power3BonusFixed: Int = 0,
                       @SerializedName("title")
                       val title: String = "",
                       @SerializedName("scenarioId")
                       val scenarioId: String = "",
                       @SerializedName("power1BonusFixed")
                       val power1BonusFixed: Int = 0,
                       @SerializedName("cardEpisodePartType")
                       val cardEpisodePartType: String = "")


