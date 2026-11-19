package com.Bolquri4.Xehvus.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import com.Bolquri4.Xehvus.domain.repository.SettingsRepository
import com.Bolquri4.Xehvus.presentation.app.AppViewModel
import com.Bolquri4.Xehvus.presentation.collection.CollectionViewModel
import com.Bolquri4.Xehvus.presentation.game.GameViewModel
import com.Bolquri4.Xehvus.presentation.gameover.GameOverViewModel
import com.Bolquri4.Xehvus.presentation.levelmap.LevelMapViewModel
import com.Bolquri4.Xehvus.presentation.menu.MenuViewModel
import com.Bolquri4.Xehvus.presentation.missions.MissionsViewModel
import com.Bolquri4.Xehvus.presentation.rewards.RewardsViewModel
import com.Bolquri4.Xehvus.presentation.settings.SettingsViewModel
import com.Bolquri4.Xehvus.presentation.splash.SplashViewModel
import com.Bolquri4.Xehvus.presentation.tutorial.TutorialViewModel

class AppViewModelFactory(
    private val progress: ProgressRepository,
    private val settings: SettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when (modelClass) {
            AppViewModel::class.java -> AppViewModel()
            SplashViewModel::class.java -> SplashViewModel(settings)
            MenuViewModel::class.java -> MenuViewModel(progress)
            TutorialViewModel::class.java -> TutorialViewModel()
            GameViewModel::class.java -> GameViewModel(progress, settings)
            GameOverViewModel::class.java -> GameOverViewModel(progress)
            LevelMapViewModel::class.java -> LevelMapViewModel(progress)
            RewardsViewModel::class.java -> RewardsViewModel(progress)
            CollectionViewModel::class.java -> CollectionViewModel(progress)
            MissionsViewModel::class.java -> MissionsViewModel(progress)
            SettingsViewModel::class.java -> SettingsViewModel(progress, settings)
            else -> AppViewModel()
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
