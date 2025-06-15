package com.haruki.kaopifeatharuki.repo.data

data class CardFilterParam(
    val isDescSort:Boolean = true,
    val sortedProperty:String,
    val filterBand:List<String>,
    val filterCharacterId:List<Int>,
    val filterAttr:List<String>,
    val filterSkillType:List<String>,
    val filterRarity:List<String>
)
