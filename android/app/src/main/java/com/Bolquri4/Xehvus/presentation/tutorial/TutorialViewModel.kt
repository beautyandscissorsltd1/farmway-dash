package com.Bolquri4.Xehvus.presentation.tutorial

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TutorialUiState(
    val step: Int = 0,
    val total: Int = 4
) {
    val isLast: Boolean get() = step == total - 1
    val isFirst: Boolean get() = step == 0
}

class TutorialViewModel : ViewModel() {

    private val _state = MutableStateFlow(TutorialUiState())
    val state: StateFlow<TutorialUiState> = _state.asStateFlow()

    fun next() {
        _state.update { it.copy(step = (it.step + 1).coerceAtMost(it.total - 1)) }
    }

    fun previous() {
        _state.update { it.copy(step = (it.step - 1).coerceAtLeast(0)) }
    }
}
