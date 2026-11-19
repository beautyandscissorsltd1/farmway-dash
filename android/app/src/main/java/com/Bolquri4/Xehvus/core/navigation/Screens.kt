package com.Bolquri4.Xehvus.core.navigation

import com.Bolquri4.Xehvus.domain.model.SessionResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Tutorial : Screen
    data class Game(val stageIndex: Int, val sessionId: Long) : Screen
    data class GameOver(val result: SessionResult) : Screen
    data object LevelMap : Screen
    data object Rewards : Screen
    data object Collection : Screen
    data object Missions : Screen
    data object Settings : Screen
}
