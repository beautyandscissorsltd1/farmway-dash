package com.Bolquri4.Xehvus.domain.model

data class PlayerProgress(
    val bestScore: Int = 0,
    val corn: Int = 0,
    val stagesUnlocked: Int = 1,
    val stageStars: Map<Int, Int> = emptyMap(),
    val stageBest: Map<Int, Int> = emptyMap(),
    val ownedRewards: Set<String> = emptySet(),
    val equippedReward: String = "",
    val unlockedCards: Set<String> = emptySet(),
    val missionSlots: List<String> = emptyList(),
    val missionProgress: Map<String, Int> = emptyMap(),
    val claimedMissions: Set<String> = emptySet()
) {
    fun starsFor(stageIndex: Int): Int = stageStars[stageIndex] ?: 0

    fun bestFor(stageIndex: Int): Int = stageBest[stageIndex] ?: 0
}

data class GameSettings(
    val leftHanded: Boolean = false,
    val laneGuides: Boolean = true,
    val hazardTelegraph: Boolean = true,
    val reduceMotion: Boolean = false
)
