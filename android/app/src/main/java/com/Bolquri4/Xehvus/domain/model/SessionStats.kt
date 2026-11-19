package com.Bolquri4.Xehvus.domain.model

data class SessionStats(
    val badges: Int = 0,
    val bestChain: Int = 0,
    val crossings: Int = 0,
    val bestTimeLeft: Int = 0,
    val corn: Int = 0,
    val highestStageCleared: Int = -1,
    val nearMisses: Int = 0,
    val fullSweeps: Int = 0
)
