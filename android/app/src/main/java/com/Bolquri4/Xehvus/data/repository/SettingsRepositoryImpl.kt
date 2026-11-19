package com.Bolquri4.Xehvus.data.repository

import com.Bolquri4.Xehvus.data.local.PreferencesStore
import com.Bolquri4.Xehvus.domain.model.GameSettings
import com.Bolquri4.Xehvus.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val store: PreferencesStore
) : SettingsRepository {

    private val _settings = MutableStateFlow(read())
    override val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    override fun update(transform: (GameSettings) -> GameSettings) {
        val next = transform(_settings.value)
        _settings.value = next
        write(next)
    }

    private fun read(): GameSettings = GameSettings(
        leftHanded = store.flag(PreferencesStore.KEY_LEFT_HANDED, false),
        laneGuides = store.flag(PreferencesStore.KEY_LANE_GUIDES, true),
        hazardTelegraph = store.flag(PreferencesStore.KEY_HAZARD, true),
        reduceMotion = store.flag(PreferencesStore.KEY_REDUCE_MOTION, false)
    )

    private fun write(value: GameSettings) {
        store.putFlag(PreferencesStore.KEY_LEFT_HANDED, value.leftHanded)
        store.putFlag(PreferencesStore.KEY_LANE_GUIDES, value.laneGuides)
        store.putFlag(PreferencesStore.KEY_HAZARD, value.hazardTelegraph)
        store.putFlag(PreferencesStore.KEY_REDUCE_MOTION, value.reduceMotion)
    }
}
