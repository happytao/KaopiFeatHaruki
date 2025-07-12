package com.haruki.kaopifeatharuki.repo.datamanager

object CardMasterRankBonusManager {
    private const val TAG = "CardMasterRankBonusManager"

    private val cardMasterRankBonusMap:Map<String, Int> = mapOf(
        "rarity_1" to 150,
        "rarity_2" to 300,
        "rarity_3" to 450,
        "rarity_4" to 600,
        "rarity_birthday" to 540
    )

    fun getCardMasterRankBonus(cardMasterRank:String, rank:Int):Int {
        if(!cardMasterRankBonusMap.containsKey(cardMasterRank))
            return 0
        return cardMasterRankBonusMap[cardMasterRank]!! * rank

    }

}