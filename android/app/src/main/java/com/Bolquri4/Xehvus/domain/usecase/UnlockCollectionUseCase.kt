package com.Bolquri4.Xehvus.domain.usecase

import com.Bolquri4.Xehvus.core.config.GameConfig
import com.Bolquri4.Xehvus.domain.model.BadgeTint
import com.Bolquri4.Xehvus.domain.model.CardSet
import com.Bolquri4.Xehvus.domain.model.CollectionCard
import com.Bolquri4.Xehvus.domain.model.PlayerProgress

class UnlockCollectionUseCase(
    private val catalog: List<CollectionCard>
) {

    data class Unlocked(val progress: PlayerProgress, val cardId: String)

    operator fun invoke(progress: PlayerProgress, tint: BadgeTint): Unlocked {
        val set = setFor(tint)
        val next = catalog.firstOrNull { it.set == set && it.id !in progress.unlockedCards }
            ?: return Unlocked(progress, "")
        val owned = progress.unlockedCards + next.id
        val completed = catalog.filter { it.set == set }.all { it.id in owned }
        val wasCompleted = catalog.filter { it.set == set }.all { it.id in progress.unlockedCards }
        val bonus = if (completed && !wasCompleted) GameConfig.SET_COMPLETE_CORN else 0
        return Unlocked(
            progress.copy(unlockedCards = owned, corn = progress.corn + bonus),
            next.id
        )
    }

    fun isSetComplete(progress: PlayerProgress, set: CardSet): Boolean =
        catalog.filter { it.set == set }.all { it.id in progress.unlockedCards }

    private fun setFor(tint: BadgeTint): CardSet = when (tint) {
        BadgeTint.DAWN -> CardSet.DAWN
        BadgeTint.HARVEST -> CardSet.HARVEST
        BadgeTint.NIGHT -> CardSet.NIGHT
    }
}
