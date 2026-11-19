package com.Bolquri4.Xehvus.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.domain.model.CardSet

@Composable
fun stageName(id: String): String = stringResource(
    when (id) {
        "dawn_pasture" -> R.string.stage_dawn_pasture
        "hay_harvest" -> R.string.stage_hay_harvest
        "market_run" -> R.string.stage_market_run
        "dusty_crossroads" -> R.string.stage_dusty_crossroads
        "storm_fields" -> R.string.stage_storm_fields
        else -> R.string.stage_night_barn
    }
)

@Composable
fun cardName(id: String): String = stringResource(
    when (id) {
        "morning_rooster" -> R.string.card_morning_rooster
        "dew_fence" -> R.string.card_dew_fence
        "first_light_barn" -> R.string.card_first_light_barn
        "hay_stack" -> R.string.card_hay_stack
        "grain_truck" -> R.string.card_grain_truck
        "windmill" -> R.string.card_windmill
        "lantern_post" -> R.string.card_lantern_post
        "moon_coop" -> R.string.card_moon_coop
        else -> R.string.card_owl_watch
    }
)

@Composable
fun setName(set: CardSet): String = stringResource(
    when (set) {
        CardSet.DAWN -> R.string.set_dawn
        CardSet.HARVEST -> R.string.set_harvest
        CardSet.NIGHT -> R.string.set_night
    }
)

@Composable
fun rewardName(id: String): String = stringResource(
    when (id) {
        "straw_hat_hen" -> R.string.reward_straw_hat_hen
        "dawn_feather" -> R.string.reward_dawn_feather
        "harvest_scarf" -> R.string.reward_harvest_scarf
        "dusty_boots" -> R.string.reward_dusty_boots
        "storm_crest" -> R.string.reward_storm_crest
        else -> R.string.reward_night_lantern
    }
)

@Composable
fun rewardEffectText(id: String): String = stringResource(
    when (id) {
        "straw_hat_hen" -> R.string.reward_effect_straw_hat_hen
        "dawn_feather" -> R.string.reward_effect_dawn_feather
        "harvest_scarf" -> R.string.reward_effect_harvest_scarf
        "dusty_boots" -> R.string.reward_effect_dusty_boots
        "storm_crest" -> R.string.reward_effect_storm_crest
        else -> R.string.reward_effect_night_lantern
    }
)

@Composable
fun missionTitle(id: String): String = stringResource(
    when (id) {
        "badge_run" -> R.string.mission_badge_run
        "rhythm_six" -> R.string.mission_rhythm_six
        "triple_crossing" -> R.string.mission_triple_crossing
        "spare_seconds" -> R.string.mission_spare_seconds
        "corn_purse" -> R.string.mission_corn_purse
        "market_clear" -> R.string.mission_market_clear
        "tight_squeeze" -> R.string.mission_tight_squeeze
        else -> R.string.mission_full_sweep
    }
)
