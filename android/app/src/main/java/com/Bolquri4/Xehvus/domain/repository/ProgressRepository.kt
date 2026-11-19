package com.Bolquri4.Xehvus.domain.repository

import com.Bolquri4.Xehvus.domain.model.PlayerProgress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<PlayerProgress>
    fun update(transform: (PlayerProgress) -> PlayerProgress)
    fun reset()
}
