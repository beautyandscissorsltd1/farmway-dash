package com.Bolquri4.Xehvus.presentation.tutorial

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.Bolquri4.Xehvus.core.theme.Cream
import com.Bolquri4.Xehvus.core.theme.RoadEarth
import com.Bolquri4.Xehvus.core.theme.Sage
import com.Bolquri4.Xehvus.core.theme.SageDeep
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.theme.TextSecondary
import com.Bolquri4.Xehvus.core.ui.PrimaryButton
import com.Bolquri4.Xehvus.core.ui.ScreenScaffold
import com.Bolquri4.Xehvus.core.ui.SecondaryButton
import com.Bolquri4.Xehvus.core.ui.WoodPanel

@Composable
fun TutorialScreen(
    onBack: () -> Unit,
    onPlay: () -> Unit
) {
    val tutorialViewModel: TutorialViewModel = viewModel(factory = ServiceLocator.factory())
    val state by tutorialViewModel.state.collectAsStateWithLifecycle()
    TutorialContent(
        state = state,
        onBack = onBack,
        onPlay = onPlay,
        onNext = tutorialViewModel::next,
        onPrevious = tutorialViewModel::previous
    )
}

@Composable
private fun TutorialContent(
    state: TutorialUiState,
    onBack: () -> Unit,
    onPlay: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    ScreenScaffold(
        title = stringResource(R.string.cta_tutorial),
        onBack = onBack,
        overlay = listOf(Cream.copy(alpha = 0.80f), Cream.copy(alpha = 0.90f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            WoodPanel(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = stringResource(stepTitle(state.step)),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    StepDiagram(step = state.step)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = stringResource(stepBody(state.step)),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(state.total) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (index == state.step) Sage else SurfaceDeep,
                                CircleShape
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (state.isLast) {
                PrimaryButton(
                    text = stringResource(R.string.cta_play),
                    onClick = onPlay,
                    leadingIcon = R.drawable.ic_hop
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton(
                    text = stringResource(R.string.cta_back),
                    onClick = onPrevious,
                    enabled = !state.isFirst,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.cta_next),
                    onClick = onNext,
                    enabled = !state.isLast,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
private fun StepDiagram(step: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(RoadEarth, RoundedCornerShape(14.dp))
    ) {
        when (step) {
            0 -> LaneStrips()
            1 -> Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = stringResource(R.string.cd_hen),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(78.dp)
            )
            2 -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = null,
                    tint = SurfaceDeep,
                    modifier = Modifier.size(28.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.cd_hen),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(70.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = SurfaceDeep,
                    modifier = Modifier.size(28.dp)
                )
            }
            else -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = stringResource(R.string.cd_truck),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(90.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_tractor),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(70.dp)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = stringResource(R.string.cd_badge),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}

@Composable
private fun LaneStrips() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(SageDeep, RoundedCornerShape(4.dp))
        )
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(SurfaceDeep.copy(alpha = 0.35f), RoundedCornerShape(3.dp))
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(BadgeGold.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
        )
    }
}

private fun stepTitle(step: Int): Int = when (step) {
    0 -> R.string.tutorial_title_road
    1 -> R.string.tutorial_title_cross
    2 -> R.string.tutorial_title_switch
    else -> R.string.tutorial_title_rhythm
}

private fun stepBody(step: Int): Int = when (step) {
    0 -> R.string.tutorial_body_road
    1 -> R.string.tutorial_body_cross
    2 -> R.string.tutorial_body_switch
    else -> R.string.tutorial_body_rhythm
}
