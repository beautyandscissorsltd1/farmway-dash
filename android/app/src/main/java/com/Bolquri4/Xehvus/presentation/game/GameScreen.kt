package com.Bolquri4.Xehvus.presentation.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.theme.Danger
import com.Bolquri4.Xehvus.core.theme.DuskDeep
import com.Bolquri4.Xehvus.core.theme.RoadNight
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.ui.GameBackground
import com.Bolquri4.Xehvus.domain.model.SessionResult
import com.Bolquri4.Xehvus.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    stageIndex: Int,
    sessionId: Long,
    onGameOver: (SessionResult) -> Unit,
    onMenu: () -> Unit,
    onTutorial: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory())
    val state by gameViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(sessionId) {
        gameViewModel.beginSession(stageIndex, sessionId)
    }
    LaunchedEffect(Unit) {
        gameViewModel.events.collect { result ->
            onGameOver(result)
        }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onHop = gameViewModel::hop,
        onLeft = gameViewModel::stepLeft,
        onRight = gameViewModel::stepRight,
        onPause = gameViewModel::pause,
        onResume = gameViewModel::resume,
        onMenu = onMenu,
        onTutorial = onTutorial
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onHop: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit,
    onTutorial: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        GameBackground(
            overlay = listOf(
                RoadNight.copy(alpha = 0.62f),
                DuskDeep.copy(alpha = 0.48f)
            )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            HudBar(state = state, onHome = onMenu, onPause = onPause)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                RoadBoard(state = state, modifier = Modifier.fillMaxSize())
            }
            if (state.hintVisible) {
                Text(
                    text = stringResource(R.string.game_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = SurfaceDeep,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
            ControlBar(
                leftHanded = state.leftHanded,
                onLeft = onLeft,
                onHop = onHop,
                onRight = onRight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 18.dp)
            )
        }
        if (state.collisionFlash) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Danger.copy(alpha = 0.28f))
            )
        }
        RushBanner(
            visible = state.rushVisible,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 96.dp)
        )
        if (state.paused) {
            PauseDialog(
                onResume = onResume,
                onTutorial = onTutorial,
                onMenu = onMenu
            )
        }
    }
}
