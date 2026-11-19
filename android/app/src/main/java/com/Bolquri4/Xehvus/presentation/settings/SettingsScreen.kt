package com.Bolquri4.Xehvus.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Bolquri4.Xehvus.core.theme.TextMuted
import com.Bolquri4.Xehvus.core.theme.TextOnDark
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.theme.TextSecondary
import com.Bolquri4.Xehvus.core.ui.DangerButton
import com.Bolquri4.Xehvus.core.ui.ScreenScaffold
import com.Bolquri4.Xehvus.core.ui.SecondaryButton
import com.Bolquri4.Xehvus.core.ui.WoodPanel
import com.Bolquri4.Xehvus.domain.model.GameSettings
import com.Bolquri4.Xehvus.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onReset: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory())
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    val asking by settingsViewModel.confirmReset.collectAsStateWithLifecycle()
    SettingsContent(
        state = state,
        asking = asking,
        onBack = onBack,
        onLeftHanded = settingsViewModel::setLeftHanded,
        onLaneGuides = settingsViewModel::toggleLaneGuides,
        onHazard = settingsViewModel::toggleHazardTelegraph,
        onReduceMotion = settingsViewModel::toggleReduceMotion,
        onAskReset = settingsViewModel::askReset,
        onCancelReset = settingsViewModel::cancelReset,
        onConfirmReset = {
            settingsViewModel.confirmResetProgress()
            onReset()
        }
    )
}

@Composable
private fun SettingsContent(
    state: GameSettings,
    asking: Boolean,
    onBack: () -> Unit,
    onLeftHanded: (Boolean) -> Unit,
    onLaneGuides: () -> Unit,
    onHazard: () -> Unit,
    onReduceMotion: () -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    ScreenScaffold(
        title = stringResource(R.string.cta_settings),
        onBack = onBack,
        overlay = listOf(Cream.copy(alpha = 0.82f), Cream.copy(alpha = 0.93f))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
        ) {
            WoodPanel(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = stringResource(R.string.settings_control_side),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SecondaryButton(
                            text = stringResource(R.string.settings_right_handed),
                            onClick = { onLeftHanded(false) },
                            enabled = state.leftHanded,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.settings_left_handed),
                            onClick = { onLeftHanded(true) },
                            enabled = !state.leftHanded,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            ToggleRow(
                title = stringResource(R.string.settings_lane_guides),
                hint = stringResource(R.string.settings_lane_guides_hint),
                checked = state.laneGuides,
                onToggle = onLaneGuides
            )
            ToggleRow(
                title = stringResource(R.string.settings_hazard),
                hint = stringResource(R.string.settings_hazard_hint),
                checked = state.hazardTelegraph,
                onToggle = onHazard
            )
            ToggleRow(
                title = stringResource(R.string.settings_reduce_motion),
                hint = stringResource(R.string.settings_reduce_motion_hint),
                checked = state.reduceMotion,
                onToggle = onReduceMotion
            )
            Spacer(modifier = Modifier.weight(1f))
            DangerButton(
                text = stringResource(R.string.cta_reset_progress),
                onClick = onAskReset
            )
            Text(
                text = stringResource(R.string.settings_footer),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
    if (asking) {
        ConfirmResetDialog(onCancel = onCancelReset, onConfirm = onConfirmReset)
    }
}

@Composable
private fun ToggleRow(
    title: String,
    hint: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)
    WoodPanel(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = if (checked) onLabel else offLabel }
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
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Text(
                    text = hint,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextOnDark,
                    checkedTrackColor = Sage,
                    uncheckedThumbColor = SurfaceDeep,
                    uncheckedTrackColor = SurfaceAlt
                )
            )
        }
    }
}
