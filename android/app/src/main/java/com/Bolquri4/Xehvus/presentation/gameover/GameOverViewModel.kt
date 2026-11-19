package com.Bolquri4.Xehvus.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class GameOverUiState(
    val bestScore: Int = 0,
    val corn: Int = 0,
    val cardsUnlocked: Int = 0
)

class GameOverViewModel(
    progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<GameOverUiState> = progress.progress
        .map { value ->
            GameOverUiState(
                bestScore = value.bestScore,
                corn = value.corn,
                cardsUnlocked = value.unlockedCards.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GameOverUiState()
        )
}
