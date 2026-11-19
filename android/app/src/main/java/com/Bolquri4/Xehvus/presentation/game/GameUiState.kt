package com.Bolquri4.Xehvus.presentation.game

import com.Bolquri4.Xehvus.domain.model.BadgeTint
import com.Bolquri4.Xehvus.domain.model.VehicleKind

data class VehicleView(
    val id: Int,
    val lane: Int,
    val kind: VehicleKind,
    val x: Float,
    val widthFraction: Float,
    val flipped: Boolean
)

data class BadgeView(
    val row: Int,
    val column: Int,
    val tint: BadgeTint
)

data class CornFlight(
    val id: Int,
    val row: Int,
    val x: Float,
    val progress: Float
)

data class GameUiState(
    val stageIndex: Int = 0,
    val routeId: Int = 0,
    val remainingSeconds: Int = 75,
    val badgesCollected: Int = 0,
    val badgesTotal: Int = 3,
    val chain: Int = 0,
    val score: Int = 0,
    val corn: Int = 0,
    val crossingIndex: Int = 1,
    val henRow: Int = 0,
    val henColumn: Int = 1,
    val visualRow: Float = 0f,
    val visualColumn: Float = 1f,
    val squash: Float = 1f,
    val vehicles: List<VehicleView> = emptyList(),
    val badges: List<BadgeView> = emptyList(),
    val cornFlights: List<CornFlight> = emptyList(),
    val laneDirections: List<Int> = emptyList(),
    val telegraphLanes: Set<Int> = emptySet(),
    val hintVisible: Boolean = true,
    val rushVisible: Boolean = false,
    val collisionFlash: Boolean = false,
    val shake: Float = 0f,
    val paused: Boolean = false,
    val ready: Boolean = false,
    val leftHanded: Boolean = false,
    val laneGuides: Boolean = true,
    val hazardTelegraph: Boolean = true,
    val reduceMotion: Boolean = false,
    val henTint: Long = 0L
)
