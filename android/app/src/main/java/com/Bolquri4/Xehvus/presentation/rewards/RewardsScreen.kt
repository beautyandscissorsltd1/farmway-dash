package com.Bolquri4.Xehvus.presentation.rewards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.theme.BadgeGold
import com.Bolquri4.Xehvus.core.theme.Cream
import com.Bolquri4.Xehvus.core.theme.SurfaceAlt
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.Terracotta
import com.Bolquri4.Xehvus.core.theme.TextMuted
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.theme.TextSecondary
import com.Bolquri4.Xehvus.core.ui.ChipStat

import com.Bolquri4.Xehvus.core.ui.ScreenScaffold
import com.Bolquri4.Xehvus.core.ui.SecondaryButton
import com.Bolquri4.Xehvus.core.ui.rewardEffectText
import com.Bolquri4.Xehvus.core.ui.rewardName

@Composable
fun RewardsScreen(onBack: () -> Unit) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = ServiceLocator.factory())
    val state by rewardsViewModel.state.collectAsStateWithLifecycle()
    RewardsContent(
        state = state,
        onBack = onBack,
        onUnlock = rewardsViewModel::unlock,
        onEquip = rewardsViewModel::equip
    )
}

@Composable
private fun RewardsContent(
    state: RewardsUiState,
    onBack: () -> Unit,
    onUnlock: (String) -> Unit,
    onEquip: (String) -> Unit
) {
    val nothingYet = state.corn == 0 && state.rows.none { it.owned }
    ScreenScaffold(
        title = stringResource(R.string.cta_rewards),
        onBack = onBack,
        overlay = listOf(Cream.copy(alpha = 0.80f), Cream.copy(alpha = 0.92f)),
        headerExtra = {
            ChipStat(
                iconRes = R.drawable.ic_corn,
                text = state.corn.toString(),
                accent = TextPrimary,
                container = SurfaceAlt,
                live = true
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (nothingYet) {
                Text(
                    text = stringResource(R.string.rewards_empty),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.rows.chunked(2).forEach { pair ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            pair.forEach { row ->
                                RewardCard(
                                    row = row,
                                    onUnlock = { onUnlock(row.id) },
                                    onEquip = { onEquip(row.id) }
                                )
                            }
                        }
                    }
                }
            }
            Text(
                text = stringResource(R.string.rewards_disclaimer),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RowScope.RewardCard(
    row: RewardRow,
    onUnlock: () -> Unit,
    onEquip: () -> Unit
) {
    val equippedLabel = stringResource(R.string.state_equipped)
    val ownedLabel = stringResource(R.string.state_owned)
    val lockedLabel = stringResource(R.string.state_locked)
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceAlt,
        border = BorderStroke(
            if (row.equipped) 2.dp else 1.5.dp,
            if (row.equipped) Terracotta else SurfaceDeep
        ),
        shadowElevation = 5.dp,
        modifier = Modifier
            .weight(1f)
            .semantics {
                stateDescription = when {
                    row.equipped -> equippedLabel
                    row.owned -> ownedLabel
                    else -> lockedLabel
                }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color(row.tint), BlendMode.Modulate),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = rewardName(row.id),
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = rewardEffectText(row.id),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            when {
                row.owned -> SecondaryButton(
                    text = if (row.equipped) {
                        stringResource(R.string.cta_equipped)
                    } else {
                        stringResource(R.string.cta_equip)
                    },
                    onClick = onEquip,
                    height = 40.dp
                )
                row.affordable -> SecondaryButton(
                    text = stringResource(R.string.cta_unlock, row.cost),
                    onClick = onUnlock,
                    height = 40.dp
                )
                else -> Text(
                    text = stringResource(R.string.rewards_need_more, row.missing),
                    style = MaterialTheme.typography.labelSmall,
                    color = BadgeGold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }
}
