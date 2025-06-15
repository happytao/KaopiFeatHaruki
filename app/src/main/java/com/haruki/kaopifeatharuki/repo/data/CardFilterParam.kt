package com.haruki.kaopifeatharuki.repo.data

data class CardFilterParam(
    val isDescSort:Boolean = true,
    val sortedProperty:String,
    val filterBands:List<String>,
    val filterCharacterIds:List<Int>,
    val filterAttrs:List<String>,
    val filterSkillTypes:List<String>,
    val filterRarities:List<String>
) {
    fun isInitState():Boolean {
        if(!isDescSort) return false
        if(sortedProperty != "release_time") return false
        if(filterBands.size != 6) return false
        if(filterCharacterIds.size != 26) return false
        if(filterAttrs.size != 5) return false
        if(filterSkillTypes.size != 6) return false
        if(filterRarities.size != 5) return false
        return true
    }
}
