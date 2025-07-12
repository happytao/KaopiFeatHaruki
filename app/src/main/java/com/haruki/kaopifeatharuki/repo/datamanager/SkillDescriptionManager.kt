package com.haruki.kaopifeatharuki.repo.datamanager

import com.haruki.kaopifeatharuki.repo.data.skill.CardSkillData
import kotlin.math.max
import kotlin.math.min

object SkillDescriptionManager {
    private const val TAG = "SkillDescriptionManager"

    fun getSkillDescription(skillData: CardSkillData, skillRank:Int = 1,
                            characterName: String = "", characterRank: Int = 1): String {
        when(skillData.id) {
            22 -> {
                return replacePlaceholders(skillData.description) { idList, char ->
                    // "description": "{{53;d}}秒内，得分提高{{53;v}}%，并根据“{{0;c}}”的角色等级再提高{{54,103;r}}%（角色等级每2级+1%，目前合计{{53,103;s}}%/最多{{53,103;v}}%）。"
                    //{{54,103;r}}% char == r 时 startId是54 与其他情况多了1 这里减1保持计算结果统一正确
                    val startId =
                        if(char == 'r')
                            idList.first() - 1
                        else
                            idList.first()
                    val endId = idList.last()
                    val skillEffect = skillData.skillEffects?.find { it.id == startId }
                    val duration = skillEffect?.skillEffectDetails
                        ?.find { it.level == skillRank }
                        ?.activateEffectDuration?.toIntIfWhole()
                    val currentSkillValue = skillEffect?.skillEffectDetails
                        ?.find { it.level == skillRank }
                        ?.activateEffectValue
                    val characterRankEnhanceId = min(characterRank / 2 + startId, endId)
                    val characterRankEnhance =
                        if(characterRankEnhanceId == startId)
                            0
                        else {
                            skillData.skillEffects
                                ?.find { it.id == characterRankEnhanceId }
                                ?.skillEffectDetails
                                ?.find { it.level == skillRank }
                                ?.activateEffectValue
                        }


                    when(char) {
                        'c' -> {
                            return@replacePlaceholders characterName
                        }
                        'r' -> {
                            return@replacePlaceholders characterRankEnhance.toString()
                        }
                        'd' -> {
                            return@replacePlaceholders duration.toString()
                        }
                        'v' -> {
                            if(idList.size == 1) {
                                return@replacePlaceholders currentSkillValue.toString()
                            } else {
                                val maxEnhanceValue = skillData.skillEffects?.last()
                                    ?.skillEffectDetails
                                    ?.find { it.level == skillRank }
                                    ?.activateEffectValue
                                if(maxEnhanceValue == null) {
                                    return@replacePlaceholders currentSkillValue.toString()
                                }
                                if(currentSkillValue == null) {
                                    return@replacePlaceholders characterRankEnhance.toString()
                                }
                                return@replacePlaceholders (currentSkillValue + maxEnhanceValue).toString()
                            }

                        }
                        's' -> {
                            if(characterRankEnhance == null) {
                                return@replacePlaceholders currentSkillValue.toString()
                            }
                            if(currentSkillValue == null) {
                                return@replacePlaceholders characterRankEnhance.toString()
                            }
                            return@replacePlaceholders (currentSkillValue + characterRankEnhance).toString()
                        }
                        else -> return@replacePlaceholders ""

                    }
                }
            }

            23 -> {
                return replacePlaceholders(skillData.description) { idList, char ->
                    val startId = idList.first()
                    val endId = idList.last()
                    val skillEffect = skillData.skillEffects?.find { it.id == startId }
                    val duration = skillEffect?.skillEffectDetails
                        ?.find { it.level == skillRank }
                        ?.activateEffectDuration?.toIntIfWhole()
                    val currentSkillValue = skillEffect?.skillEffectDetails
                        ?.find { it.level == skillRank }
                        ?.activateEffectValue
                    val maxEnhanceValue = skillData.skillEffects
                        ?.find { it.id == endId }
                        ?.skillEffectDetails?.last()
                        ?.activateEffectValue2
                    when(char) {
                        'd' -> {
                            return@replacePlaceholders duration.toString()
                        }
                        'v' -> {
                            return@replacePlaceholders currentSkillValue.toString()
                        }
                        'o' -> {
                            if(maxEnhanceValue == null)
                                return@replacePlaceholders currentSkillValue.toString()
                            if(currentSkillValue == null)
                                return@replacePlaceholders ""
                            return@replacePlaceholders (currentSkillValue + maxEnhanceValue).toString()

                        }
                        else -> return@replacePlaceholders ""
                    }
                }
            }

            24 -> {
                return replacePlaceholders(skillData.description) { idList, char ->
                    val startId = idList.first()
                    val endId = idList.last()
                    val skillEffect = skillData.skillEffects?.find { it.id == startId }
                    val currentSkillValue = skillEffect?.skillEffectDetails
                        ?.find { it.level == skillRank }
                        ?.activateEffectValue
                    val maxEnhanceValue = skillData.skillEffects
                        ?.find { it.id == endId }
                        ?.skillEffectDetails?.last()
                        ?.activateEffectValue
                    when(char) {
                        'v' -> {
                            return@replacePlaceholders currentSkillValue.toString()
                        }
                        'u' -> {
                            if(maxEnhanceValue == null)
                                return@replacePlaceholders currentSkillValue.toString()
                            if(currentSkillValue == null)
                                return@replacePlaceholders ""
                            return@replacePlaceholders (currentSkillValue + maxEnhanceValue).toString()

                        }
                        else -> return@replacePlaceholders ""

                    }

                }
            }

            else -> {
                return replacePlaceholders(skillData.description) { idList, char ->
                    val id = idList.first()
                    val skillEffect = skillData.skillEffects?.find { it.id == id }
                    val duration = skillEffect?.skillEffectDetails
                        ?.find { it.level == skillRank }
                        ?.activateEffectDuration?.toIntIfWhole()
                    val currentSkillValue = skillEffect?.skillEffectDetails
                        ?.find { it.level == skillRank }
                        ?.activateEffectValue
                    val enhanceValue = skillEffect?.skillEnhance?.activateEffectValue
                    when(char) {
                        'e' -> {
                            return@replacePlaceholders enhanceValue.toString()
                        }
                        'm' -> {
                            if(currentSkillValue != null && enhanceValue != null) {
                                return@replacePlaceholders (currentSkillValue + enhanceValue * 6).toString()
                            } else return@replacePlaceholders ""
                        }
                        'd' -> {
                            return@replacePlaceholders duration.toString()
                        }
                        'v' -> {
                            return@replacePlaceholders currentSkillValue.toString()
                        }
                        else -> return@replacePlaceholders ""

                    }
                }
            }

        }
    }


    private fun replacePlaceholders(
        input: String,
        replacementProvider: (List<Int>, Char) -> String  // 参数改为 List<Int> 和 Char
    ): String {
        // 修改后的正则表达式，匹配 {{数字1,数字2;字母}} 或 {{数字;字母}} 格式
        val regex = Regex("\\{\\{(\\d+(?:,\\d+)*);([a-zA-Z])\\}\\}")

        return regex.replace(input) { matchResult ->
            val (numbersStr, char) = matchResult.destructured
            val numbers = numbersStr.split(',').map { it.toInt() }  // 分割多个数字
            replacementProvider(numbers, char.first())
        }
    }

    fun Float.toIntIfWhole(): Number {
        return if (this % 1 == 0f) this.toInt() else this
    }



}