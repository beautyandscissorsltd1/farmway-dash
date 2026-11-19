package com.Bolquri4.Xehvus.domain.usecase

import com.Bolquri4.Xehvus.core.config.GameConfig
import com.Bolquri4.Xehvus.domain.model.RoadMath
import com.Bolquri4.Xehvus.domain.model.RoadPlan

class ValidateRoadPlanUseCase {

    operator fun invoke(plan: RoadPlan): Boolean = grade(plan) == GRADE_PERFECT

    fun grade(plan: RoadPlan): Int {
        if (!everyColumnBreathes(plan)) return GRADE_UNUSABLE
        val run = greedyRunner(plan)
        if (!run.reachedBarn) return GRADE_UNUSABLE
        return when {
            run.badges >= plan.badges.size -> GRADE_PERFECT
            run.badges >= plan.badges.size - 1 -> GRADE_GOOD
            else -> GRADE_PASSABLE
        }
    }

    private fun everyColumnBreathes(plan: RoadPlan): Boolean {
        for (lane in plan.lanes) {
            if (lane.vehicles.isEmpty()) continue
            for (column in 0 until GameConfig.COLUMNS) {
                var block = 0
                while (block < BLOCKS) {
                    if (!hasWindow(plan, lane.index, column, block * BLOCK_SECONDS)) return false
                    block++
                }
            }
        }
        return true
    }

    private fun hasWindow(plan: RoadPlan, laneIndex: Int, column: Int, from: Float): Boolean {
        var t = from
        var free = 0f
        val end = from + BLOCK_SECONDS
        while (t <= end) {
            if (RoadMath.laneOccupied(plan, laneIndex, column, t)) {
                free = 0f
            } else {
                free += STEP
                if (free >= WINDOW_SECONDS) return true
            }
            t += STEP
        }
        return false
    }

    private data class RunnerResult(val reachedBarn: Boolean, val badges: Int)

    private fun greedyRunner(plan: RoadPlan): RunnerResult {
        var time = 0f
        var row = 0
        var column = 1
        var cooldown = 0f
        val collected = HashSet<Int>()

        fun inLane(): Boolean = row in 1 until GameConfig.ROWS - 1

        fun safeHere(target: Int, horizon: Float): Boolean =
            !inLane() || RoadMath.laneSafeFor(plan, row - 1, target, time, horizon)

        fun safeAhead(target: Int, horizon: Float): Boolean =
            row + 1 == GameConfig.ROWS - 1 ||
                RoadMath.laneSafeFor(plan, row, target, time, horizon)

        fun collectHere() {
            plan.badges.forEachIndexed { index, badge ->
                if (badge.row == row && badge.column == column) collected.add(index)
            }
        }

        while (time < RUNNER_BUDGET_SECONDS && row < GameConfig.ROWS - 1) {
            if (inLane() && RoadMath.laneOccupied(plan, row - 1, column, time)) {
                return RunnerResult(false, collected.size)
            }
            collectHere()
            if (cooldown > 0f) {
                cooldown -= STEP
                time += STEP
                continue
            }
            if (!safeHere(column, DANGER_HORIZON)) {
                if (safeAhead(column, DANGER_HORIZON)) {
                    row += 1
                    cooldown = HOP_COOLDOWN
                    time += STEP
                    continue
                }
                var dodged = false
                for (delta in SIDES) {
                    val next = column + delta
                    if (next in 0 until GameConfig.COLUMNS && safeHere(next, DODGE_HORIZON)) {
                        column = next
                        cooldown = SIDE_COOLDOWN
                        dodged = true
                        break
                    }
                }
                if (dodged) {
                    time += STEP
                    continue
                }
                if (safeAhead(column, ESCAPE_HORIZON)) {
                    row += 1
                    cooldown = HOP_COOLDOWN
                    time += STEP
                }
                time += STEP
                continue
            }
            val wanted = wantedColumn(plan, row, collected, column)
            if (wanted != column) {
                val next = if (wanted > column) column + 1 else column - 1
                if (safeHere(next, SIDE_HORIZON)) {
                    column = next
                    cooldown = SIDE_COOLDOWN
                    time += STEP
                    continue
                }
            }
            if (safeAhead(column, HOP_HORIZON)) {
                row += 1
                cooldown = HOP_COOLDOWN
                time += STEP
                continue
            }
            time += STEP
        }
        collectHere()
        return RunnerResult(row == GameConfig.ROWS - 1, collected.size)
    }

    private fun wantedColumn(
        plan: RoadPlan,
        row: Int,
        collected: Set<Int>,
        current: Int
    ): Int {
        var bestRow = Int.MAX_VALUE
        var bestColumn = current
        plan.badges.forEachIndexed { index, badge ->
            if (index !in collected && badge.row > row && badge.row < bestRow) {
                bestRow = badge.row
                bestColumn = badge.column
            }
        }
        return bestColumn
    }

    companion object {
        const val GRADE_UNUSABLE = 0
        const val GRADE_PASSABLE = 1
        const val GRADE_GOOD = 2
        const val GRADE_PERFECT = 3

        private const val STEP = 0.05f
        private const val BLOCK_SECONDS = 6f
        private const val BLOCKS = 12
        private const val WINDOW_SECONDS = 0.65f
        private const val RUNNER_BUDGET_SECONDS = 45f
        private const val HOP_HORIZON = 1.25f
        private const val SIDE_HORIZON = 0.5f
        private const val DANGER_HORIZON = 0.9f
        private const val DODGE_HORIZON = 1.4f
        private const val ESCAPE_HORIZON = 0.45f
        private const val HOP_COOLDOWN = 0.18f
        private const val SIDE_COOLDOWN = 0.14f
        private val SIDES = intArrayOf(-1, 1)
    }
}
