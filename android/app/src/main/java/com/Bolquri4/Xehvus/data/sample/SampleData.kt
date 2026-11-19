package com.Bolquri4.Xehvus.data.sample

import com.Bolquri4.Xehvus.domain.model.BadgeTint
import com.Bolquri4.Xehvus.domain.model.CardSet
import com.Bolquri4.Xehvus.domain.model.CollectionCard
import com.Bolquri4.Xehvus.domain.model.Mission
import com.Bolquri4.Xehvus.domain.model.MissionKind
import com.Bolquri4.Xehvus.domain.model.RewardEffect
import com.Bolquri4.Xehvus.domain.model.RewardItem
import com.Bolquri4.Xehvus.domain.model.Stage

object SampleData {

    val stages: List<Stage> = listOf(
        Stage(0, "dawn_pasture", 1.00f, 3, 0),
        Stage(1, "hay_harvest", 1.10f, 4, 0),
        Stage(2, "market_run", 1.20f, 5, 0),
        Stage(3, "dusty_crossroads", 1.32f, 5, 2),
        Stage(4, "storm_fields", 1.45f, 5, 2),
        Stage(5, "night_barn", 1.60f, 5, 3)
    )

    val missions: List<Mission> = listOf(
        Mission("badge_run", MissionKind.ROUTE_BADGES, 12, 60),
        Mission("rhythm_six", MissionKind.RHYTHM_CHAIN, 6, 80),
        Mission("triple_crossing", MissionKind.CROSSINGS, 3, 80),
        Mission("spare_seconds", MissionKind.TIME_LEFT, 20, 60),
        Mission("corn_purse", MissionKind.CORN_PURSE, 30, 60),
        Mission("market_clear", MissionKind.STAGE_CLEAR, 3, 100),
        Mission("tight_squeeze", MissionKind.NEAR_MISSES, 5, 80),
        Mission("full_sweep", MissionKind.FULL_SWEEP, 1, 100)
    )

    val cards: List<CollectionCard> = listOf(
        CollectionCard("morning_rooster", CardSet.DAWN, BadgeTint.DAWN),
        CollectionCard("dew_fence", CardSet.DAWN, BadgeTint.DAWN),
        CollectionCard("first_light_barn", CardSet.DAWN, BadgeTint.DAWN),
        CollectionCard("hay_stack", CardSet.HARVEST, BadgeTint.HARVEST),
        CollectionCard("grain_truck", CardSet.HARVEST, BadgeTint.HARVEST),
        CollectionCard("windmill", CardSet.HARVEST, BadgeTint.HARVEST),
        CollectionCard("lantern_post", CardSet.NIGHT, BadgeTint.NIGHT),
        CollectionCard("moon_coop", CardSet.NIGHT, BadgeTint.NIGHT),
        CollectionCard("owl_watch", CardSet.NIGHT, BadgeTint.NIGHT)
    )

    val rewards: List<RewardItem> = listOf(
        RewardItem("straw_hat_hen", 40, 0xFFD9A441, RewardEffect.TINT),
        RewardItem("dawn_feather", 60, 0xFFF2C879, RewardEffect.TRAIL),
        RewardItem("harvest_scarf", 90, 0xFF7FA46B, RewardEffect.TINT),
        RewardItem("dusty_boots", 120, 0xFFC9A77C, RewardEffect.DUST),
        RewardItem("storm_crest", 160, 0xFF5B6C8C, RewardEffect.TINT),
        RewardItem("night_lantern", 220, 0xFFF7E0A8, RewardEffect.GLOW)
    )

    fun stageAt(index: Int): Stage = stages.getOrElse(index) { stages.first() }

    fun cardsOf(set: CardSet): List<CollectionCard> = cards.filter { it.set == set }

    fun cardSetOf(tint: BadgeTint): CardSet = when (tint) {
        BadgeTint.DAWN -> CardSet.DAWN
        BadgeTint.HARVEST -> CardSet.HARVEST
        BadgeTint.NIGHT -> CardSet.NIGHT
    }

    fun missionById(id: String): Mission? = missions.firstOrNull { it.id == id }

    fun rewardById(id: String): RewardItem? = rewards.firstOrNull { it.id == id }
}
