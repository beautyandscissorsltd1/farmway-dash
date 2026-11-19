package com.Bolquri4.Xehvus.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.data.sample.SampleData
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class RewardRow(
    val id: String,
    val cost: Int,
    val tint: Long,
    val owned: Boolean,
    val equipped: Boolean,
    val affordable: Boolean,
    val missing: Int
)

data class RewardsUiState(
    val rows: List<RewardRow> = emptyList(),
    val corn: Int = 0
)

class RewardsViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<RewardsUiState> = progress.progress
        .map { value ->
            RewardsUiState(
                rows = SampleData.rewards.map { item ->
                    RewardRow(
                        id = item.id,
                        cost = item.cost,
                        tint = item.tint,
                        owned = item.id in value.ownedRewards,
                        equipped = item.id == value.equippedReward,
                        affordable = value.corn >= item.cost,
                        missing = (item.cost - value.corn).coerceAtLeast(0)
                    )
                },
                corn = value.corn
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RewardsUiState()
        )

    fun unlock(rewardId: String) {
        progress.update { ServiceLocator.purchaseReward.unlock(it, rewardId) }
    }

    fun equip(rewardId: String) {
        progress.update { ServiceLocator.purchaseReward.equip(it, rewardId) }
    }
}
