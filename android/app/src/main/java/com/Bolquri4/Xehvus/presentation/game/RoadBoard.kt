package com.Bolquri4.Xehvus.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.config.GameConfig
import com.Bolquri4.Xehvus.core.theme.BadgeGold
import com.Bolquri4.Xehvus.core.theme.Danger
import com.Bolquri4.Xehvus.core.theme.NightBlue
import com.Bolquri4.Xehvus.core.theme.RoadEarth
import com.Bolquri4.Xehvus.core.theme.Sage
import com.Bolquri4.Xehvus.core.theme.SageDeep
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.Terracotta
import com.Bolquri4.Xehvus.core.theme.Wood
import com.Bolquri4.Xehvus.core.theme.WoodLight
import com.Bolquri4.Xehvus.domain.model.BadgeTint
import com.Bolquri4.Xehvus.domain.model.VehicleKind
import kotlin.math.floor

@Composable
fun RoadBoard(
    state: GameUiState,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val frame: Dp = (GameConfig.BOARD_PADDING_DP + GameConfig.BOARD_BORDER_DP).dp
        val widthBudget = minOf(maxWidth - 32.dp, GameConfig.BOARD_MAX_WIDTH_DP.dp)
        val tileW: Dp = floor((widthBudget - frame * 2).value / GameConfig.COLUMNS).dp
        val boardW: Dp = tileW * GameConfig.COLUMNS + frame * 2
        val heightBudget = maxHeight
        val rowH: Dp = minOf(
            GameConfig.BOARD_ROW_MAX_DP.dp,
            floor((heightBudget - frame * 2).value / GameConfig.ROWS).dp
        )
        val boardH: Dp = rowH * GameConfig.ROWS + frame * 2
        val contentW = tileW * GameConfig.COLUMNS

        Box(
            modifier = Modifier
                .width(boardW)
                .height(boardH)
                .clip(RoundedCornerShape(18.dp))
                .background(RoadEarth, RoundedCornerShape(18.dp))
                .border(GameConfig.BOARD_BORDER_DP.dp, Wood, RoundedCornerShape(18.dp))
        ) {
            Box(
                modifier = Modifier
                    .offset(x = frame, y = frame)
                    .width(contentW)
                    .height(rowH * GameConfig.ROWS)
            ) {
                SafeRow(rowIndex = 0, rowH = rowH, contentW = contentW)
                SafeRow(rowIndex = GameConfig.ROWS - 1, rowH = rowH, contentW = contentW)
                LaneDecor(state = state, rowH = rowH, contentW = contentW, tileW = tileW)
                Vehicles(state = state, rowH = rowH, contentW = contentW)
                Badges(state = state, rowH = rowH, tileW = tileW)
                Hen(state = state, rowH = rowH, tileW = tileW)
            }
        }
    }
}

@Composable
private fun SafeRow(rowIndex: Int, rowH: Dp, contentW: Dp) {
    val y = rowH * (GameConfig.ROWS - 1 - rowIndex)
    Box(
        modifier = Modifier
            .offset(y = y)
            .width(contentW)
            .height(rowH)
            .background(SageDeep.copy(alpha = 0.85f))
    ) {
        Box(
            modifier = Modifier
                .offset(y = rowH / 2)
                .fillMaxWidth()
                .height(3.dp)
                .background(WoodLight.copy(alpha = 0.55f))
        )
    }
}

@Composable
private fun LaneDecor(state: GameUiState, rowH: Dp, contentW: Dp, tileW: Dp) {
    for (lane in 0 until GameConfig.LANES) {
        val row = lane + 1
        val y = rowH * (GameConfig.ROWS - 1 - row)
        Box(
            modifier = Modifier
                .offset(y = y)
                .width(contentW)
                .height(2.dp)
                .background(SurfaceDeep.copy(alpha = 0.35f))
        )
        if (state.laneGuides) {
            val direction = state.laneDirections.getOrNull(lane) ?: 1
            Icon(
                painter = painterResource(
                    if (direction > 0) R.drawable.ic_arrow_right else R.drawable.ic_arrow_left
                ),
                contentDescription = null,
                tint = SurfaceDeep.copy(alpha = 0.5f),
                modifier = Modifier
                    .offset(
                        x = if (direction > 0) contentW - 18.dp else 4.dp,
                        y = y + rowH / 2 - 7.dp
                    )
                    .size(14.dp)
            )
        }
        if (state.hazardTelegraph && lane in state.telegraphLanes) {
            val direction = state.laneDirections.getOrNull(lane) ?: 1
            Box(
                modifier = Modifier
                    .offset(x = if (direction > 0) 0.dp else contentW - 4.dp, y = y)
                    .width(4.dp)
                    .height(rowH)
                    .background(Terracotta)
            )
        }
    }
    if (state.laneGuides) {
        for (column in 1 until GameConfig.COLUMNS) {
            Box(
                modifier = Modifier
                    .offset(x = tileW * column, y = rowH)
                    .width(1.dp)
                    .height(rowH * GameConfig.LANES)
                    .background(SurfaceDeep.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
private fun Vehicles(state: GameUiState, rowH: Dp, contentW: Dp) {
    state.vehicles.forEach { vehicle ->
        val row = vehicle.lane + 1
        val y = rowH * (GameConfig.ROWS - 1 - row)
        val width = contentW * vehicle.widthFraction
        val left = contentW * vehicle.x - width / 2f
        val height = rowH * 0.74f
        Image(
            painter = painterResource(
                if (vehicle.kind == VehicleKind.TRUCK) R.drawable.sprite_2 else R.drawable.sprite_3
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .offset(x = left, y = y + (rowH - height) / 2f)
                .width(width)
                .height(height)
                .graphicsLayer { scaleX = if (vehicle.flipped) -1f else 1f }
        )
    }
}

@Composable
private fun Badges(state: GameUiState, rowH: Dp, tileW: Dp) {
    state.badges.forEach { badge ->
        val y = rowH * (GameConfig.ROWS - 1 - badge.row)
        val x = tileW * badge.column
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(x = x, y = y)
                .width(tileW)
                .height(rowH)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(tintOf(badge.tint).copy(alpha = 0.22f), CircleShape)
            )
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_badge),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(tintOf(badge.tint), BlendMode.Modulate),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun Hen(state: GameUiState, rowH: Dp, tileW: Dp) {
    val henSize = 44.dp
    val x = tileW * state.visualColumn + (tileW - henSize) / 2f
    val y = rowH * (GameConfig.ROWS - 1 - state.visualRow) + (rowH - henSize) / 2f
    val tint = state.henTint
    Image(
        painter = painterResource(R.drawable.sprite_1),
        contentDescription = stringResource(R.string.cd_hen),
        contentScale = ContentScale.Fit,
        colorFilter = if (tint != 0L) {
            ColorFilter.tint(Color(tint), BlendMode.Modulate)
        } else if (state.collisionFlash) {
            ColorFilter.tint(Danger, BlendMode.Modulate)
        } else {
            null
        },
        modifier = Modifier
            .offset(x = x, y = y)
            .size(henSize)
            .scale(state.squash)
    )
}

private fun tintOf(tint: BadgeTint): Color = when (tint) {
    BadgeTint.DAWN -> BadgeGold
    BadgeTint.HARVEST -> Sage
    BadgeTint.NIGHT -> NightBlue
}
