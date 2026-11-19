package com.Bolquri4.Xehvus.domain.model

enum class RewardEffect { TINT, TRAIL, DUST, GLOW }

data class RewardItem(
    val id: String,
    val cost: Int,
    val tint: Long,
    val effect: RewardEffect
)
