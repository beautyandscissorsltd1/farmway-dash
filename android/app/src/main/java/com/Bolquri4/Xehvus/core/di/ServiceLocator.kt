package com.Bolquri4.Xehvus.core.di

import android.content.Context
import com.Bolquri4.Xehvus.data.local.PreferencesStore
import com.Bolquri4.Xehvus.data.repository.ProgressRepositoryImpl
import com.Bolquri4.Xehvus.data.repository.SettingsRepositoryImpl
import com.Bolquri4.Xehvus.data.sample.SampleData
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import com.Bolquri4.Xehvus.domain.repository.SettingsRepository
import com.Bolquri4.Xehvus.domain.usecase.AwardStarsUseCase
import com.Bolquri4.Xehvus.domain.usecase.GenerateRoadPlanUseCase
import com.Bolquri4.Xehvus.domain.usecase.PurchaseRewardUseCase
import com.Bolquri4.Xehvus.domain.usecase.UnlockCollectionUseCase
import com.Bolquri4.Xehvus.domain.usecase.UpdateMissionsUseCase
import com.Bolquri4.Xehvus.domain.usecase.ValidateRoadPlanUseCase

object ServiceLocator {

    private var progressRepository: ProgressRepository? = null
    private var settingsRepository: SettingsRepository? = null

    val generateRoadPlan: GenerateRoadPlanUseCase = GenerateRoadPlanUseCase(ValidateRoadPlanUseCase())
    val updateMissions: UpdateMissionsUseCase = UpdateMissionsUseCase(SampleData.missions)
    val unlockCollection: UnlockCollectionUseCase = UnlockCollectionUseCase(SampleData.cards)
    val purchaseReward: PurchaseRewardUseCase = PurchaseRewardUseCase(SampleData.rewards)
    val awardStars: AwardStarsUseCase = AwardStarsUseCase()

    fun init(context: Context) {
        if (progressRepository != null) return
        val store = PreferencesStore(context.applicationContext)
        progressRepository = ProgressRepositoryImpl(store, SampleData.stages.size)
        settingsRepository = SettingsRepositoryImpl(store)
    }

    fun progress(): ProgressRepository = checkNotNull(progressRepository)

    fun settings(): SettingsRepository = checkNotNull(settingsRepository)

    fun factory(): AppViewModelFactory = AppViewModelFactory(progress(), settings())
}
