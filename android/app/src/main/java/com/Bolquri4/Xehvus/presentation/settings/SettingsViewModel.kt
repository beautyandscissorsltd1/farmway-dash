package com.Bolquri4.Xehvus.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.domain.model.GameSettings
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import com.Bolquri4.Xehvus.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    val state: StateFlow<GameSettings> = settings.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = settings.settings.value
        )

    private val _confirmReset = MutableStateFlow(false)
    val confirmReset: StateFlow<Boolean> = _confirmReset.asStateFlow()

    fun setLeftHanded(value: Boolean) {
        settings.update { it.copy(leftHanded = value) }
    }

    fun toggleLaneGuides() {
        settings.update { it.copy(laneGuides = !it.laneGuides) }
    }

    fun toggleHazardTelegraph() {
        settings.update { it.copy(hazardTelegraph = !it.hazardTelegraph) }
    }

    fun toggleReduceMotion() {
        settings.update { it.copy(reduceMotion = !it.reduceMotion) }
    }

    fun askReset() {
        _confirmReset.value = true
    }

    fun cancelReset() {
        _confirmReset.value = false
    }

    fun confirmResetProgress() {
        _confirmReset.value = false
        progress.reset()
    }
}
