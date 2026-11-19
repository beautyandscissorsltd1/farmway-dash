package com.Bolquri4.Xehvus.presentation.levelmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.data.sample.SampleData
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StageNode(
    val index: Int,
    val id: String,
    val unlocked: Boolean,
    val stars: Int,
    val best: Int,
    val speed: Float,
    val badges: Int
)

data class LevelMapUiState(
    val nodes: List<StageNode> = emptyList(),
    val corn: Int = 0,
    val selected: Int = 0,
    val lockedNotice: Boolean = false
)

class LevelMapViewModel(
    private val progress: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelMapUiState())
    val state: StateFlow<LevelMapUiState> = _state.asStateFlow()

    private var noticeJob: Job? = null

    init {
        viewModelScope.launch {
            progress.progress.collect { value ->
                val nodes = SampleData.stages.map { stage ->
                    StageNode(
                        index = stage.index,
                        id = stage.id,
                        unlocked = stage.index < value.stagesUnlocked,
                        stars = value.starsFor(stage.index),
                        best = value.bestFor(stage.index),
                        speed = stage.speedMultiplier,
                        badges = 3
                    )
                }
                _state.update { current ->
                    current.copy(
                        nodes = nodes,
                        corn = value.corn,
                        selected = current.selected.coerceAtMost(nodes.size - 1)
                    )
                }
            }
        }
    }

    fun select(index: Int) {
        val node = _state.value.nodes.getOrNull(index) ?: return
        if (!node.unlocked) {
            showLockedNotice()
            return
        }
        _state.update { it.copy(selected = index, lockedNotice = false) }
    }

    private fun showLockedNotice() {
        noticeJob?.cancel()
        _state.update { it.copy(lockedNotice = true) }
        noticeJob = viewModelScope.launch {
            delay(1600)
            _state.update { it.copy(lockedNotice = false) }
        }
    }

    override fun onCleared() {
        noticeJob?.cancel()
        super.onCleared()
    }
}
