package com.Bolquri4.Xehvus.domain.usecase

import com.Bolquri4.Xehvus.domain.model.PlayerProgress
import com.Bolquri4.Xehvus.domain.model.RewardItem

class PurchaseRewardUseCase(
    private val catalog: List<RewardItem>
) {

    fun unlock(progress: PlayerProgress, rewardId: String): PlayerProgress {
        val item = catalog.firstOrNull { it.id == rewardId } ?: return progress
        if (item.id in progress.ownedRewards || progress.corn < item.cost) return progress
        return progress.copy(
            corn = progress.corn - item.cost,
            ownedRewards = progress.ownedRewards + item.id,
            equippedReward = item.id
        )
    }

    fun equip(progress: PlayerProgress, rewardId: String): PlayerProgress {
        if (rewardId !in progress.ownedRewards) return progress
        val next = if (progress.equippedReward == rewardId) "" else rewardId
        return progress.copy(equippedReward = next)
    }

    fun equippedTint(progress: PlayerProgress): Long =
        catalog.firstOrNull { it.id == progress.equippedReward }?.tint ?: 0L
}
