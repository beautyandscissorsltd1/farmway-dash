package com.Bolquri4.Xehvus.domain.model

enum class MissionKind {
    ROUTE_BADGES,
    RHYTHM_CHAIN,
    CROSSINGS,
    TIME_LEFT,
    CORN_PURSE,
    STAGE_CLEAR,
    NEAR_MISSES,
    FULL_SWEEP
}

data class Mission(
    val id: String,
    val kind: MissionKind,
    val target: Int,
    val reward: Int
)

data class MissionSlot(
    val mission: Mission,
    val progress: Int,
    val claimable: Boolean
)
