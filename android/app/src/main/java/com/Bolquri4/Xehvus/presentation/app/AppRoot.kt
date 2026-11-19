package com.Bolquri4.Xehvus.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.navigation.Screen
import com.Bolquri4.Xehvus.core.theme.Cream
import com.Bolquri4.Xehvus.presentation.collection.CollectionScreen
import com.Bolquri4.Xehvus.presentation.game.GameScreen
import com.Bolquri4.Xehvus.presentation.gameover.GameOverScreen
import com.Bolquri4.Xehvus.presentation.levelmap.LevelMapScreen
import com.Bolquri4.Xehvus.presentation.menu.MenuScreen
import com.Bolquri4.Xehvus.presentation.missions.MissionsScreen
import com.Bolquri4.Xehvus.presentation.rewards.RewardsScreen
import com.Bolquri4.Xehvus.presentation.settings.SettingsScreen
import com.Bolquri4.Xehvus.presentation.splash.SplashScreen
import com.Bolquri4.Xehvus.presentation.tutorial.TutorialScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory())
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen !is Screen.Menu && screen !is Screen.Splash) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(tween(260)) togetherWith fadeOut(tween(220))
            },
            label = "screenHost"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.navigate(Screen.Menu) }
                )

                is Screen.Menu -> MenuScreen(
                    onPlay = { appViewModel.openGame(0) },
                    onLevels = { appViewModel.navigate(Screen.LevelMap) },
                    onTutorial = { appViewModel.navigate(Screen.Tutorial) },
                    onMissions = { appViewModel.navigate(Screen.Missions) },
                    onCollection = { appViewModel.navigate(Screen.Collection) },
                    onRewards = { appViewModel.navigate(Screen.Rewards) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                is Screen.Tutorial -> TutorialScreen(
                    onBack = { appViewModel.back() },
                    onPlay = { appViewModel.openGame(0, replace = true) }
                )

                is Screen.Game -> GameScreen(
                    stageIndex = current.stageIndex,
                    sessionId = current.sessionId,
                    onGameOver = { appViewModel.navigate(Screen.GameOver(it), replace = true) },
                    onMenu = { appViewModel.toMenu() },
                    onTutorial = { appViewModel.navigate(Screen.Tutorial) }
                )

                is Screen.GameOver -> GameOverScreen(
                    result = current.result,
                    onPlayAgain = { appViewModel.openGame(current.result.stageIndex, replace = true) },
                    onLevels = { appViewModel.navigate(Screen.LevelMap, replace = true) },
                    onMenu = { appViewModel.toMenu() }
                )

                is Screen.LevelMap -> LevelMapScreen(
                    onBack = { appViewModel.back() },
                    onPlayStage = { appViewModel.openGame(it) }
                )

                is Screen.Rewards -> RewardsScreen(onBack = { appViewModel.back() })

                is Screen.Collection -> CollectionScreen(onBack = { appViewModel.back() })

                is Screen.Missions -> MissionsScreen(onBack = { appViewModel.back() })

                is Screen.Settings -> SettingsScreen(
                    onBack = { appViewModel.back() },
                    onReset = { appViewModel.toMenu() }
                )
            }
        }
    }
}
