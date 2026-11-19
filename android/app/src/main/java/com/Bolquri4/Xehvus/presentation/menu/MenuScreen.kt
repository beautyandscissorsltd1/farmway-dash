package com.Bolquri4.Xehvus.presentation.menu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.theme.BadgeGold
import com.Bolquri4.Xehvus.core.theme.Cream
import com.Bolquri4.Xehvus.core.theme.Sage
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.SurfaceWarm
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.theme.TextSecondary
import com.Bolquri4.Xehvus.core.theme.Wood
import com.Bolquri4.Xehvus.core.theme.WoodLight
import com.Bolquri4.Xehvus.core.ui.GameBackground
import com.Bolquri4.Xehvus.core.ui.formatScore
import com.Bolquri4.Xehvus.core.ui.PrimaryButton
import com.Bolquri4.Xehvus.core.ui.SecondaryButton
import com.Bolquri4.Xehvus.core.ui.StatCardData
import com.Bolquri4.Xehvus.core.ui.StatCardRow

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onTutorial: () -> Unit,
    onMissions: () -> Unit,
    onCollection: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory())
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuContent(
        state = state,
        onPlay = onPlay,
        onLevels = onLevels,
        onTutorial = onTutorial,
        onMissions = onMissions,
        onCollection = onCollection,
        onRewards = onRewards,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onTutorial: () -> Unit,
    onMissions: () -> Unit,
    onCollection: () -> Unit,
    onRewards: () -> Unit,
    onSettings: () -> Unit
) {
    val reveal by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(320),
        label = "menuReveal"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        GameBackground(
            overlay = listOf(
                Cream.copy(alpha = 0.05f),
                Cream.copy(alpha = 0.35f),
                SurfaceWarm.copy(alpha = 0.92f)
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Wood,
                    shadowElevation = 10.dp,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(WoodLight.copy(alpha = 0.5f))
                        )
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = stringResource(R.string.cd_logo),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(16.dp)
                                .width(128.dp)
                        )
                    }
                }
            }
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = SurfaceWarm.copy(alpha = 0.97f),
                shadowElevation = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(reveal)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(4.dp)
                            .background(SurfaceDeep, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = stringResource(R.string.brand_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.brand_tagline),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    StatCardRow(
                        cards = listOf(
                            StatCardData(
                                label = stringResource(R.string.stat_best),
                                value = formatScore(state.bestScore),
                                accent = Sage,
                                visible = state.bestScore > 0
                            ),
                            StatCardData(
                                label = stringResource(R.string.stat_corn),
                                value = state.corn.toString(),
                                accent = BadgeGold,
                                visible = state.corn > 0
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    PrimaryButton(
                        text = stringResource(R.string.cta_play),
                        onClick = onPlay,
                        leadingIcon = R.drawable.ic_hop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    MenuRow(
                        leftText = stringResource(R.string.cta_levels),
                        leftIcon = R.drawable.ic_star,
                        onLeft = onLevels,
                        rightText = stringResource(R.string.cta_tutorial),
                        rightIcon = R.drawable.ic_hop,
                        onRight = onTutorial
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MenuRow(
                        leftText = stringResource(R.string.cta_missions),
                        leftIcon = R.drawable.ic_mission,
                        onLeft = onMissions,
                        rightText = stringResource(R.string.cta_collection),
                        rightIcon = R.drawable.ic_card,
                        onRight = onCollection
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MenuRow(
                        leftText = stringResource(R.string.cta_rewards),
                        leftIcon = R.drawable.ic_corn,
                        onLeft = onRewards,
                        rightText = stringResource(R.string.cta_settings),
                        rightIcon = R.drawable.ic_check,
                        onRight = onSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuRow(
    leftText: String,
    leftIcon: Int,
    onLeft: () -> Unit,
    rightText: String,
    rightIcon: Int,
    onRight: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SecondaryButton(
            text = leftText,
            onClick = onLeft,
            leadingIcon = leftIcon,
            modifier = Modifier.weight(1f)
        )
        SecondaryButton(
            text = rightText,
            onClick = onRight,
            leadingIcon = rightIcon,
            modifier = Modifier.weight(1f)
        )
    }
}

