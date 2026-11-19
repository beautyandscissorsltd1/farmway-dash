package com.Bolquri4.Xehvus.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.theme.BadgeGold
import com.Bolquri4.Xehvus.core.theme.Sage
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.Terracotta
import com.Bolquri4.Xehvus.core.ui.ChipStat

@Composable
fun HudBar(
    state: GameUiState,
    onHome: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            TextButton(
                onClick = onHome,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.cd_home),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = stringResource(R.string.hud_route, state.routeId),
                style = MaterialTheme.typography.labelSmall,
                color = SurfaceDeep
            )
            TextButton(
                onClick = onPause,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_pause),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ChipStat(
                iconRes = R.drawable.ic_clock,
                text = stringResource(R.string.hud_time, state.remainingSeconds),
                accent = if (state.remainingSeconds <= 10) Terracotta else SurfaceDeep,
                live = true
            )
            ChipStat(
                iconRes = R.drawable.ic_badge,
                text = stringResource(
                    R.string.hud_badges,
                    state.badgesCollected,
                    state.badgesTotal
                ),
                accent = BadgeGold,
                live = true
            )
            if (state.chain >= 1) {
                ChipStat(
                    iconRes = R.drawable.ic_hop,
                    text = stringResource(R.string.hud_chain, state.chain),
                    accent = Sage
                )
            }
            CornChip(state.corn)
        }
    }
}

@Composable
private fun CornChip(value: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = BadgeGold
        )
    }
}
