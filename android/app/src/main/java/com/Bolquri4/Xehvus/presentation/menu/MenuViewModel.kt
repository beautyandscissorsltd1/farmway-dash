package com.Bolquri4.Xehvus.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.data.sample.SampleData
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<MenuUiState> = progress.progress
        .map { value ->
            MenuUiState(
                bestScore = value.bestScore,
                corn = value.corn,
                stagesUnlocked = value.stagesUnlocked,
                cardsUnlocked = value.unlockedCards.size,
                cardsTotal = SampleData.cards.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MenuUiState(cardsTotal = SampleData.cards.size)
        )
}
