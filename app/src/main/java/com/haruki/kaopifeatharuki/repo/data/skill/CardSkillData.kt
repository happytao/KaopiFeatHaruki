package com.haruki.kaopifeatharuki.repo.data.skill


import com.google.gson.annotations.SerializedName

data class CardSkillData(@SerializedName("descriptionSpriteName")
                         val descriptionSpriteName: String = "",
                         @SerializedName("skillFilterId")
                         val skillFilterId: Int = 0,
                         @SerializedName("description")
                         val description: String = "",
                         @SerializedName("id")
                         val id: Int = 0,
                         @SerializedName("skillEffects")
                         val skillEffects: List<SkillEffectsItem>?)


data class SkillEffectsItem(@SerializedName("skillEnhance")
                            val skillEnhance:SkillEnhance?,
                            @SerializedName("activateNotesJudgmentType")
                            val activateNotesJudgmentType: String = "",
                            @SerializedName("skillEffectDetails")
                            val skillEffectDetails: List<SkillEffectDetailsItem>?,
                            @SerializedName("activateLife")
                            val activateLife: Int = 0,
                            @SerializedName("skillEffectType")
                            val skillEffectType: String = "",
                            @SerializedName("conditionType")
                            val conditionType: String = "",
                            @SerializedName("id")
                            val id: Int = 0)


data class SkillEffectDetailsItem(@SerializedName("activateEffectValue")
                                  val activateEffectValue: Int = 0,
                                  @SerializedName("level")
                                  val level: Int = 0,
                                  @SerializedName("id")
                                  val id: Int = 0,
                                  @SerializedName("activateEffectDuration")
                                  val activateEffectDuration: Float = 0f,
                                  @SerializedName("activateEffectValueType")
                                  val activateEffectValueType: String = "")



data class SkillEnhance(@SerializedName("activateEffectValue")
                        val activateEffectValue: Int = 0,
                        @SerializedName("skillEnhanceType")
                        val skillEnhanceType: String = "",
                        @SerializedName("id")
                        val id: Int = 0,
                        @SerializedName("skillEnhanceCondition")
                        val skillEnhanceCondition: SkillEnhanceCondition,
                        @SerializedName("activateEffectValueType")
                        val activateEffectValueType: String = "")

data class SkillEnhanceCondition(@SerializedName("unit")
                                 val unit: String = "",
                                 @SerializedName("id")
                                 val id: Int = 0,
                                 @SerializedName("seq")
                                 val seq: Int = 0)

