package com.Bolquri4.Xehvus.domain.repository

import com.Bolquri4.Xehvus.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>
    fun update(transform: (GameSettings) -> GameSettings)
}
