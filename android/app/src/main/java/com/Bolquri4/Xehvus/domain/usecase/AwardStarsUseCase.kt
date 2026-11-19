package com.Bolquri4.Xehvus.domain.usecase

import com.Bolquri4.Xehvus.domain.model.PlayerProgress

class AwardStarsUseCase {

    fun starsFor(reachedBarn: Boolean, allBadges: Boolean, bestChain: Int): Int = when {
        !reachedBarn -> 0
        allBadges && bestChain >= 5 -> 3
        allBadges -> 2
        else -> 1
    }

    fun apply(
        progress: PlayerProgress,
        stageIndex: Int,
        stars: Int,
        score: Int,
        totalStages: Int
    ): PlayerProgress {
        val stars0 = maxOf(progress.starsFor(stageIndex), stars)
        val best0 = maxOf(progress.bestFor(stageIndex), score)
        val unlocked = if (stars > 0) {
            maxOf(progress.stagesUnlocked, minOf(stageIndex + 2, totalStages))
        } else {
            progress.stagesUnlocked
        }
        return progress.copy(
            stagesUnlocked = unlocked,
            stageStars = progress.stageStars + (stageIndex to stars0),
            stageBest = progress.stageBest + (stageIndex to best0),
            bestScore = maxOf(progress.bestScore, score)
        )
    }
}
