package com.Bolquri4.Xehvus.presentation.levelmap

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.theme.Cream
import com.Bolquri4.Xehvus.core.theme.Sage
import com.Bolquri4.Xehvus.core.theme.SurfaceAlt
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.Terracotta
import com.Bolquri4.Xehvus.core.theme.TextMuted
import com.Bolquri4.Xehvus.core.theme.TextOnDark
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.theme.TextSecondary
import com.Bolquri4.Xehvus.core.theme.Wood
import com.Bolquri4.Xehvus.core.ui.ChipStat
import com.Bolquri4.Xehvus.core.ui.PrimaryButton
import com.Bolquri4.Xehvus.core.ui.ScreenScaffold
import com.Bolquri4.Xehvus.core.ui.StarRow
import com.Bolquri4.Xehvus.core.ui.WoodPanel
import com.Bolquri4.Xehvus.core.ui.formatScore
import com.Bolquri4.Xehvus.core.ui.stageName

@Composable
fun LevelMapScreen(
    onBack: () -> Unit,
    onPlayStage: (Int) -> Unit
) {
    val levelMapViewModel: LevelMapViewModel = viewModel(factory = ServiceLocator.factory())
    val state by levelMapViewModel.state.collectAsStateWithLifecycle()
    LevelMapContent(
        state = state,
        onBack = onBack,
        onSelect = levelMapViewModel::select,
        onPlayStage = onPlayStage
    )
}

@Composable
private fun LevelMapContent(
    state: LevelMapUiState,
    onBack: () -> Unit,
    onSelect: (Int) -> Unit,
    onPlayStage: (Int) -> Unit
) {
    val selected = state.nodes.getOrNull(state.selected)
    ScreenScaffold(
        title = stringResource(R.string.cta_levels),
        onBack = onBack,
        overlay = listOf(Cream.copy(alpha = 0.78f), Cream.copy(alpha = 0.90f)),
        headerExtra = {
            ChipStat(
                iconRes = R.drawable.ic_corn,
                text = state.corn.toString(),
                accent = TextPrimary,
                container = SurfaceAlt
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                DirtTrack(modifier = Modifier.fillMaxSize())
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    state.nodes.forEach { node ->
                        StageRow(
                            node = node,
                            current = node.index == state.selected,
                            onSelect = { onSelect(node.index) }
                        )
                    }
                }
            }
            if (state.lockedNotice) {
                Text(
                    text = stringResource(R.string.level_locked_notice),
                    style = MaterialTheme.typography.labelSmall,
                    color = Terracotta,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            if (selected != null) {
                WoodPanel(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = stageName(selected.id),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(
                                R.string.level_detail,
                                selected.speed,
                                selected.badges,
                                formatScore(selected.best)
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PrimaryButton(
                            text = stringResource(R.string.cta_play),
                            onClick = { onPlayStage(selected.index) },
                            leadingIcon = R.drawable.ic_hop,
                            height = 56.dp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StageRow(
    node: StageNode,
    current: Boolean,
    onSelect: () -> Unit
) {
    val lockedLabel = stringResource(R.string.state_locked)
    val openLabel = stringResource(R.string.state_unlocked)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (node.index % 2 == 0) Arrangement.Start else Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (node.unlocked) SurfaceAlt else SurfaceDeep.copy(alpha = 0.5f),
            border = BorderStroke(if (current) 2.dp else 1.dp, if (current) Terracotta else Wood),
            shadowElevation = if (node.unlocked) 6.dp else 0.dp,
            modifier = Modifier
                .clickable(onClick = onSelect)
                .semantics { stateDescription = if (node.unlocked) openLabel else lockedLabel }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .height(58.dp)
                    .padding(horizontal = 14.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        shape = CircleShape,
                        color = if (node.unlocked) Sage else TextMuted,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = (node.index + 1).toString(),
                                style = MaterialTheme.typography.labelLarge,
                                color = TextOnDark
                            )
                        }
                    }
                }
                Column {
                    Text(
                        text = stageName(node.id),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (node.unlocked) TextPrimary else TextMuted
                    )
                    StarRow(earned = node.stars, total = 3, starSize = 12)
                }
            }
        }
    }
}

@Composable
private fun DirtTrack(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path()
        val left = size.width * 0.22f
        val right = size.width * 0.78f
        val stepY = size.height / 6f
        path.moveTo(left, stepY * 0.5f)
        var index = 0
        while (index < 5) {
            val fromX = if (index % 2 == 0) left else right
            val toX = if (index % 2 == 0) right else left
            val fromY = stepY * (index + 0.5f)
            val toY = stepY * (index + 1.5f)
            path.cubicTo(
                fromX + (toX - fromX) * 0.4f,
                fromY,
                fromX + (toX - fromX) * 0.6f,
                toY,
                toX,
                toY
            )
            index += 1
        }
        drawPath(
            path = path,
            color = Wood.copy(alpha = 0.35f),
            style = Stroke(width = 10f)
        )
        drawCircle(
            color = Wood.copy(alpha = 0.18f),
            radius = 8f,
            center = Offset(left, stepY * 0.5f)
        )
    }
}
