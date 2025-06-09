package com.haruki.kaopifeatharuki.repo.data

import com.google.gson.annotations.SerializedName

data class Cost(@SerializedName("resourceId")
                val resourceId: Int = 0,
                @SerializedName("quantity")
                val quantity: Int = 0,
                @SerializedName("resourceLevel")
                val resourceLevel: Int = 0,
                @SerializedName("resourceType")
                val resourceType: String = "")