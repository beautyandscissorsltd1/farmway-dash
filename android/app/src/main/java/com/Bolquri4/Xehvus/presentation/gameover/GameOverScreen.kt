package com.Bolquri4.Xehvus.presentation.gameover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.theme.BadgeGold
import com.Bolquri4.Xehvus.core.theme.DuskDeep
import com.Bolquri4.Xehvus.core.theme.NightBlue
import com.Bolquri4.Xehvus.core.theme.Sage
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.Terracotta
import com.Bolquri4.Xehvus.core.theme.TextOnDark
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.ui.PrimaryButton
import com.Bolquri4.Xehvus.core.ui.SecondaryButton
import com.Bolquri4.Xehvus.core.ui.StarRow
import com.Bolquri4.Xehvus.core.ui.StatCardData
import com.Bolquri4.Xehvus.core.ui.StatCardRow
import com.Bolquri4.Xehvus.core.ui.WoodPanel
import com.Bolquri4.Xehvus.core.ui.GameBackground
import com.Bolquri4.Xehvus.core.ui.cardName
import com.Bolquri4.Xehvus.core.ui.formatScore
import com.Bolquri4.Xehvus.core.ui.stageName
import com.Bolquri4.Xehvus.data.sample.SampleData
import com.Bolquri4.Xehvus.domain.model.EndReason
import com.Bolquri4.Xehvus.domain.model.SessionResult

@Composable
fun GameOverScreen(
    result: SessionResult,
    onPlayAgain: () -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory())
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    GameOverContent(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onLevels = onLevels,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    result: SessionResult,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onLevels: () -> Unit,
    onMenu: () -> Unit
) {
    val stage = SampleData.stageAt(result.stageIndex)
    Box(modifier = Modifier.fillMaxSize()) {
        GameBackground(
            overlay = listOf(
                DuskDeep.copy(alpha = 0.78f),
                Terracotta.copy(alpha = 0.26f),
                DuskDeep.copy(alpha = 0.86f)
            )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(titleFor(result)),
                style = MaterialTheme.typography.displayMedium,
                color = TextOnDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .height(4.dp)
                    .background(Terracotta, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(
                    R.string.result_subline,
                    result.routeId,
                    stage.index + 1,
                    stageName(stage.id)
                ),
                style = MaterialTheme.typography.labelSmall,
                color = SurfaceDeep,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            StarRow(earned = result.stars, total = 3, starSize = 26)
            Spacer(modifier = Modifier.height(16.dp))
            StatCardRow(
                cards = listOf(
                    StatCardData(
                        label = stringResource(R.string.stat_score),
                        value = formatScore(result.score),
                        accent = Sage
                    ),
                    StatCardData(
                        label = stringResource(R.string.stat_stage),
                        value = (stage.index + 1).toString(),
                        accent = Terracotta
                    )
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            StatCardRow(
                cards = listOf(
                    StatCardData(
                        label = stringResource(R.string.stat_badges),
                        value = result.badges.toString(),
                        accent = BadgeGold,
                        visible = result.badges > 0
                    ),
                    StatCardData(
                        label = stringResource(R.string.stat_chain),
                        value = result.bestChain.toString(),
                        accent = NightBlue,
                        visible = result.bestChain > 0
                    ),
                    StatCardData(
                        label = stringResource(R.string.stat_corn),
                        value = result.corn.toString(),
                        accent = BadgeGold,
                        visible = result.corn > 0
                    )
                )
            )
            if (result.newBest && result.score > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.result_new_best, formatScore(state.bestScore)),
                    style = MaterialTheme.typography.labelSmall,
                    color = BadgeGold
                )
            }
            if (result.newCardId.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                NewCardPanel(cardId = result.newCardId)
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.cta_play_again),
                onClick = onPlayAgain,
                leadingIcon = R.drawable.ic_hop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton(
                    text = stringResource(R.string.cta_levels),
                    onClick = onLevels,
                    leadingIcon = R.drawable.ic_star,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.cta_menu),
                    onClick = onMenu,
                    leadingIcon = R.drawable.ic_back,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NewCardPanel(cardId: String) {
    WoodPanel(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(14.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(42.dp)
            )
            Column {
                Text(
                    text = stringResource(R.string.result_new_card),
                    style = MaterialTheme.typography.labelSmall,
                    color = Terracotta
                )
                Text(
                    text = cardName(cardId),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }
        }
    }
}

private fun titleFor(result: SessionResult): Int = when {
    result.reason == EndReason.FLATTENED -> R.string.result_flattened
    result.crossingsCleared > 0 -> R.string.result_barn_reached
    else -> R.string.result_out_of_time
}
