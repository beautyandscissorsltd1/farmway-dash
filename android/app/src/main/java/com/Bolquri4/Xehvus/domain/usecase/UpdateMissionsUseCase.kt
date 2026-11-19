package com.Bolquri4.Xehvus.domain.usecase

import com.Bolquri4.Xehvus.domain.model.Mission
import com.Bolquri4.Xehvus.domain.model.MissionKind
import com.Bolquri4.Xehvus.domain.model.MissionSlot
import com.Bolquri4.Xehvus.domain.model.PlayerProgress
import com.Bolquri4.Xehvus.domain.model.SessionStats

class UpdateMissionsUseCase(
    private val catalog: List<Mission>
) {

    private fun byId(id: String): Mission? = catalog.firstOrNull { it.id == id }

    fun ensureSlots(progress: PlayerProgress): PlayerProgress {
        val slots = progress.missionSlots.filter { id ->
            byId(id) != null && id !in progress.claimedMissions
        }
        if (slots.size >= SLOT_COUNT) return progress.copy(missionSlots = slots.take(SLOT_COUNT))
        val pool = catalog
            .map { it.id }
            .filter { it !in slots && it !in progress.claimedMissions }
        return progress.copy(missionSlots = slots + pool.take(SLOT_COUNT - slots.size))
    }

    fun applySession(progress: PlayerProgress, stats: SessionStats): PlayerProgress {
        val ready = ensureSlots(progress)
        val advanced = ready.missionProgress.toMutableMap()
        ready.missionSlots.forEach { id ->
            val mission = byId(id) ?: return@forEach
            val current = advanced[id] ?: 0
            advanced[id] = (current + contribution(mission, stats)).coerceAtMost(mission.target)
        }
        return ready.copy(missionProgress = advanced)
    }

    fun claim(progress: PlayerProgress, missionId: String): PlayerProgress {
        val mission = byId(missionId) ?: return progress
        val done = (progress.missionProgress[missionId] ?: 0) >= mission.target
        if (!done || missionId in progress.claimedMissions) return progress
        val cleared = progress.copy(
            corn = progress.corn + mission.reward,
            claimedMissions = progress.claimedMissions + missionId,
            missionSlots = progress.missionSlots.filter { it != missionId }
        )
        return ensureSlots(cleared)
    }

    fun slots(progress: PlayerProgress): List<MissionSlot> =
        ensureSlots(progress).missionSlots.mapNotNull { id ->
            val mission = byId(id) ?: return@mapNotNull null
            val value = (progress.missionProgress[id] ?: 0).coerceAtMost(mission.target)
            MissionSlot(mission, value, value >= mission.target)
        }

    private fun contribution(mission: Mission, stats: SessionStats): Int = when (mission.kind) {
        MissionKind.ROUTE_BADGES -> stats.badges
        MissionKind.RHYTHM_CHAIN -> if (stats.bestChain >= mission.target) mission.target else 0
        MissionKind.CROSSINGS -> if (stats.crossings >= mission.target) mission.target else 0
        MissionKind.TIME_LEFT -> if (stats.bestTimeLeft >= mission.target) mission.target else 0
        MissionKind.CORN_PURSE -> stats.corn
        MissionKind.STAGE_CLEAR -> if (stats.highestStageCleared >= mission.target - 1) mission.target else 0
        MissionKind.NEAR_MISSES -> stats.nearMisses
        MissionKind.FULL_SWEEP -> stats.fullSweeps
    }

    private companion object {
        const val SLOT_COUNT = 3
    }
}
