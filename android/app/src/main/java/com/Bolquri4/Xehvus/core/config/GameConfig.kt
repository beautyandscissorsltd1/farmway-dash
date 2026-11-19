package com.Bolquri4.Xehvus.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val CROSSING_TIME_MS = 75_000L

    const val COLUMNS = 3
    const val ROWS = 7
    const val LANES = 5

    const val TICK_MS = 16L
    const val HOP_MS = 180L
    const val SIDE_STEP_MS = 140L

    const val BADGES_PER_CROSSING = 3
    const val SOLVE_ATTEMPTS = 20

    const val BASE_SPEED = 0.28f
    const val TRUCK_WIDTH = 0.27f
    const val TRACTOR_WIDTH = 0.23f
    const val HEN_HALF_WIDTH = 0.065f
    const val NEAR_MISS_GAP = 0.082f

    const val SPAWN_MIN = -0.45f
    const val SPAWN_MAX = 1.45f

    const val CHAIN_WINDOW_SEC = 0.4f
    const val MAX_CHAIN_MULTIPLIER = 5f
    const val CHAIN_STEP = 0.5f

    const val RUSH_STEP = 0.12f
    const val RUSH_CAP = 2.0f
    const val MAX_SPEED_MULTIPLIER = 2.4f

    const val BADGE_SCORE = 100
    const val CROSSING_SCORE = 500
    const val TIME_BONUS_PER_SECOND = 10
    const val BADGE_CORN = 2
    const val NEAR_MISS_CORN = 1
    const val SET_COMPLETE_CORN = 50

    const val COLLISION_HOLD_MS = 700L
    const val RUSH_BANNER_MS = 1200L
    const val HINT_VISIBLE_MS = 12_000L
    const val TELEGRAPH_SEC = 0.7f

    const val BOARD_PADDING_DP = 8
    const val BOARD_BORDER_DP = 2
    const val BOARD_MAX_WIDTH_DP = 360
    const val BOARD_ROW_MAX_DP = 68
    const val CONTROL_BAR_HEIGHT_DP = 108
}
