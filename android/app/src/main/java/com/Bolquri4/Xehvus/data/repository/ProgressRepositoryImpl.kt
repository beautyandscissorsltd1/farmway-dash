package com.Bolquri4.Xehvus.data.repository

import com.Bolquri4.Xehvus.data.local.PreferencesStore
import com.Bolquri4.Xehvus.domain.model.PlayerProgress
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(
    private val store: PreferencesStore,
    private val stageCount: Int
) : ProgressRepository {

    private val _progress = MutableStateFlow(read())
    override val progress: StateFlow<PlayerProgress> = _progress.asStateFlow()

    override fun update(transform: (PlayerProgress) -> PlayerProgress) {
        val next = transform(_progress.value)
        _progress.value = next
        write(next)
    }

    override fun reset() {
        store.clearAll()
        _progress.value = PlayerProgress()
    }

    private fun read(): PlayerProgress {
        val stars = HashMap<Int, Int>()
        val best = HashMap<Int, Int>()
        for (index in 0 until stageCount) {
            val earned = store.int(PreferencesStore.KEY_STAGE_STARS + index, 0)
            if (earned > 0) stars[index] = earned
            val top = store.int(PreferencesStore.KEY_STAGE_BEST + index, 0)
            if (top > 0) best[index] = top
        }
        return PlayerProgress(
            bestScore = store.int(PreferencesStore.KEY_BEST_SCORE, 0),
            corn = store.int(PreferencesStore.KEY_CORN, 0),
            stagesUnlocked = store.int(PreferencesStore.KEY_STAGE_UNLOCKED, 1),
            stageStars = stars,
            stageBest = best,
            ownedRewards = store.list(PreferencesStore.KEY_OWNED_REWARDS).toSet(),
            equippedReward = store.text(PreferencesStore.KEY_EQUIPPED_REWARD, ""),
            unlockedCards = store.list(PreferencesStore.KEY_CARDS).toSet(),
            missionSlots = store.list(PreferencesStore.KEY_MISSION_SLOTS),
            missionProgress = decodeCounts(store.list(PreferencesStore.KEY_MISSION_PROGRESS)),
            claimedMissions = store.list(PreferencesStore.KEY_MISSIONS_CLAIMED).toSet()
        )
    }

    private fun write(value: PlayerProgress) {
        store.putInt(PreferencesStore.KEY_BEST_SCORE, value.bestScore)
        store.putInt(PreferencesStore.KEY_CORN, value.corn)
        store.putInt(PreferencesStore.KEY_STAGE_UNLOCKED, value.stagesUnlocked)
        for (index in 0 until stageCount) {
            store.putInt(PreferencesStore.KEY_STAGE_STARS + index, value.starsFor(index))
            store.putInt(PreferencesStore.KEY_STAGE_BEST + index, value.bestFor(index))
        }
        store.putList(PreferencesStore.KEY_OWNED_REWARDS, value.ownedRewards)
        store.putText(PreferencesStore.KEY_EQUIPPED_REWARD, value.equippedReward)
        store.putList(PreferencesStore.KEY_CARDS, value.unlockedCards)
        store.putList(PreferencesStore.KEY_MISSION_SLOTS, value.missionSlots)
        store.putList(PreferencesStore.KEY_MISSION_PROGRESS, encodeCounts(value.missionProgress))
        store.putList(PreferencesStore.KEY_MISSIONS_CLAIMED, value.claimedMissions)
    }

    private fun encodeCounts(values: Map<String, Int>): List<String> =
        values.map { entry -> entry.key + PAIR_SEPARATOR + entry.value }

    private fun decodeCounts(values: List<String>): Map<String, Int> {
        val result = HashMap<String, Int>()
        values.forEach { raw ->
            val parts = raw.split(PAIR_SEPARATOR)
            if (parts.size == 2) {
                val count = parts[1].toIntOrNull()
                if (count != null) result[parts[0]] = count
            }
        }
        return result
    }

    private companion object {
        const val PAIR_SEPARATOR = "="
    }
}
