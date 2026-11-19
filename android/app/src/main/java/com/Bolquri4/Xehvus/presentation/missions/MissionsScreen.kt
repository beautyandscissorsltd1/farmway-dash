package com.Bolquri4.Xehvus.presentation.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.theme.BadgeGold
import com.Bolquri4.Xehvus.core.theme.Cream
import com.Bolquri4.Xehvus.core.theme.SurfaceAlt
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.ui.ChipStat
import com.Bolquri4.Xehvus.core.ui.EmptyState
import com.Bolquri4.Xehvus.core.ui.ProgressMeter
import com.Bolquri4.Xehvus.core.ui.ScreenScaffold
import com.Bolquri4.Xehvus.core.ui.SecondaryButton
import com.Bolquri4.Xehvus.core.ui.WoodPanel
import com.Bolquri4.Xehvus.core.ui.missionTitle

@Composable
fun MissionsScreen(onBack: () -> Unit) {
    val missionsViewModel: MissionsViewModel = viewModel(factory = ServiceLocator.factory())
    val state by missionsViewModel.state.collectAsStateWithLifecycle()
    MissionsContent(
        state = state,
        onBack = onBack,
        onClaim = missionsViewModel::claim
    )
}

@Composable
private fun MissionsContent(
    state: MissionsUiState,
    onBack: () -> Unit,
    onClaim: (String) -> Unit
) {
    ScreenScaffold(
        title = stringResource(R.string.cta_missions),
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (state.allCleared) {
                EmptyState(
                    iconRes = R.drawable.ic_mission,
                    message = stringResource(R.string.missions_empty)
                )
            }
            state.rows.forEach { row ->
                MissionPanel(row = row, onClaim = { onClaim(row.id) })
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun MissionPanel(row: MissionRow, onClaim: () -> Unit) {
    val readyLabel = stringResource(R.string.state_ready)
    val runningLabel = stringResource(R.string.state_running)
    WoodPanel(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = if (row.claimable) readyLabel else runningLabel }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = missionTitle(row.id),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgressMeter(
                    progress = row.progress.toFloat() / row.target.toFloat(),
                    label = stringResource(R.string.mission_progress, row.progress, row.target)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.mission_reward, row.reward),
                    style = MaterialTheme.typography.labelSmall,
                    color = BadgeGold
                )
            }
            SecondaryButton(
                text = if (row.claimable) {
                    stringResource(R.string.cta_claim)
                } else {
                    stringResource(R.string.cta_in_progress)
                },
                onClick = onClaim,
                enabled = row.claimable,
                height = 48.dp,
                modifier = Modifier.width(112.dp)
            )
        }
    }
}
