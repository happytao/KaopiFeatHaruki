package com.haruki.kaopifeatharuki.repo.data

import com.google.gson.annotations.SerializedName

data class CardParameters(@SerializedName("param3")
                          val param3: List<Integer>?,
                          @SerializedName("param1")
                          val param1: List<Integer>?,
                          @SerializedName("param2")
                          val param2: List<Integer>?)