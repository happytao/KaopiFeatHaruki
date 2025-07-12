package com.haruki.kaopifeatharuki.repo.data.card

import com.google.gson.annotations.SerializedName

data class SpecialTrainingCostsItem(@SerializedName("cost")
                                    val cost: Cost,
                                    @SerializedName("cardId")
                                    val cardId: Int = 0,
                                    @SerializedName("seq")
                                    val seq: Int = 0)