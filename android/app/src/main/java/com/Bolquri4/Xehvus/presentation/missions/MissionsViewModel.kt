package com.Bolquri4.Xehvus.presentation.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MissionRow(
    val id: String,
    val progress: Int,
    val target: Int,
    val reward: Int,
    val claimable: Boolean
)

data class MissionsUiState(
    val rows: List<MissionRow> = emptyList(),
    val corn: Int = 0,
    val allCleared: Boolean = false
)

class MissionsViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<MissionsUiState> = progress.progress
        .map { value ->
            val slots = ServiceLocator.updateMissions.slots(value)
            MissionsUiState(
                rows = slots.map { slot ->
                    MissionRow(
                        id = slot.mission.id,
                        progress = slot.progress,
                        target = slot.mission.target,
                        reward = slot.mission.reward,
                        claimable = slot.claimable
                    )
                },
                corn = value.corn,
                allCleared = slots.isEmpty()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MissionsUiState()
        )

    fun claim(missionId: String) {
        progress.update { ServiceLocator.updateMissions.claim(it, missionId) }
    }
}
