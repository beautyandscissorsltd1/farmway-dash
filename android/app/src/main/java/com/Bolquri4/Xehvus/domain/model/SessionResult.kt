package com.Bolquri4.Xehvus.domain.model

enum class EndReason { FLATTENED, OUT_OF_TIME }

data class SessionResult(
    val stageIndex: Int,
    val routeId: Int,
    val reason: EndReason,
    val score: Int,
    val badges: Int,
    val bestChain: Int,
    val corn: Int,
    val crossingsCleared: Int,
    val stars: Int,
    val newCardId: String,
    val newBest: Boolean
)
