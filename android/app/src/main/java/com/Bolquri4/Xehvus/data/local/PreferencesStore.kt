package com.Bolquri4.Xehvus.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun flag(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putFlag(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun text(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun putText(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun list(key: String): List<String> {
        val raw = text(key, "")
        if (raw.isEmpty()) return emptyList()
        return raw.split(SEPARATOR).filter { it.isNotEmpty() }
    }

    fun putList(key: String, values: Collection<String>) {
        putText(key, values.joinToString(SEPARATOR))
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val STORE_NAME = "farmway_dash_store"
        const val SEPARATOR = "|"

        const val KEY_BEST_SCORE = "best_score"
        const val KEY_CORN = "corn"
        const val KEY_STAGE_UNLOCKED = "stage_unlocked"
        const val KEY_STAGE_STARS = "stage_stars_"
        const val KEY_STAGE_BEST = "stage_best_"
        const val KEY_OWNED_REWARDS = "owned_rewards"
        const val KEY_EQUIPPED_REWARD = "equipped_reward"
        const val KEY_CARDS = "cards_unlocked"
        const val KEY_MISSION_SLOTS = "mission_slots"
        const val KEY_MISSION_PROGRESS = "mission_progress"
        const val KEY_MISSIONS_CLAIMED = "missions_claimed"
        const val KEY_LEFT_HANDED = "left_handed"
        const val KEY_LANE_GUIDES = "lane_guides"
        const val KEY_HAZARD = "hazard_telegraph"
        const val KEY_REDUCE_MOTION = "reduce_motion"
    }
}
