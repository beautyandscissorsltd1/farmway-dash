package com.Bolquri4.Xehvus.domain.usecase

import com.Bolquri4.Xehvus.core.config.GameConfig
import com.Bolquri4.Xehvus.domain.model.BadgeCell
import com.Bolquri4.Xehvus.domain.model.BadgeTint
import com.Bolquri4.Xehvus.domain.model.Lane
import com.Bolquri4.Xehvus.domain.model.RoadPlan
import com.Bolquri4.Xehvus.domain.model.Stage
import com.Bolquri4.Xehvus.domain.model.VehicleKind
import com.Bolquri4.Xehvus.domain.model.VehicleSpec
import kotlin.random.Random

class GenerateRoadPlanUseCase(
    private val validate: ValidateRoadPlanUseCase
) {

    operator fun invoke(stage: Stage, attempt: Int, rushMultiplier: Float): RoadPlan {
        val seed = stage.index * 7919 xor (attempt * 104729)
        var tries = 0
        var best: RoadPlan? = null
        var bestGrade = ValidateRoadPlanUseCase.GRADE_UNUSABLE
        while (tries < GameConfig.SOLVE_ATTEMPTS) {
            val candidate = build(stage, seed + tries * 31, rushMultiplier)
            val grade = validate.grade(candidate)
            if (grade == ValidateRoadPlanUseCase.GRADE_PERFECT) return candidate
            if (grade > bestGrade) {
                bestGrade = grade
                best = candidate
            }
            tries++
        }
        return best ?: fallback(seed, effectiveSpeed(stage, rushMultiplier))
    }

    private fun effectiveSpeed(stage: Stage, rushMultiplier: Float): Float =
        (stage.speedMultiplier * rushMultiplier).coerceAtMost(GameConfig.MAX_SPEED_MULTIPLIER)

    private fun build(stage: Stage, seed: Int, rushMultiplier: Float): RoadPlan {
        val random = Random(seed)
        val activeLanes = stage.activeLanes.coerceIn(1, GameConfig.LANES)
        val busyLanes = stage.doubleVehicleLanes.coerceIn(0, GameConfig.LANES)
        val laneOrder = (0 until GameConfig.LANES).shuffled(random)
        val liveLanes = laneOrder.take(activeLanes).toSet()
        val doubleLanes = laneOrder.filter { it in liveLanes }.take(busyLanes).toSet()

        var vehicleId = 0
        val lanes = (0 until GameConfig.LANES).map { index ->
            val direction = if ((index + random.nextInt(0, 2)) % 2 == 0) 1 else -1
            val speedFactor = 0.85f + random.nextFloat() * 0.40f
            val count = when {
                index !in liveLanes -> 0
                index in doubleLanes -> 2
                else -> 1
            }
            val vehicles = (0 until count).map { slot ->
                val kind = if (random.nextInt(0, 100) < 55) VehicleKind.TRUCK else VehicleKind.TRACTOR
                val phase = (slot.toFloat() / count.coerceAtLeast(1)) + random.nextFloat() * 0.3f
                VehicleSpec(vehicleId++, kind, phase - kotlin.math.floor(phase))
            }
            Lane(index, direction, speedFactor, vehicles)
        }

        val badges = badgeCells(random)
        return RoadPlan(seed, lanes, badges, effectiveSpeed(stage, rushMultiplier))
    }

    private fun badgeCells(random: Random): List<BadgeCell> {
        val rows = (1..GameConfig.LANES).shuffled(random)
            .take(GameConfig.BADGES_PER_CROSSING)
            .sorted()
        val tint = BadgeTint.entries[random.nextInt(0, BadgeTint.entries.size)]
        val cells = ArrayList<BadgeCell>(rows.size)
        var previous = -1
        rows.forEach { row ->
            val options = (0 until GameConfig.COLUMNS).filter { it != previous }
            val column = options[random.nextInt(0, options.size)]
            previous = column
            val cellTint = if (random.nextInt(0, 100) < 70) tint else {
                BadgeTint.entries[random.nextInt(0, BadgeTint.entries.size)]
            }
            cells.add(BadgeCell(row, column, cellTint))
        }
        return cells
    }

    private fun fallback(seed: Int, speedMultiplier: Float): RoadPlan {
        val phases = listOf(0.0f, 0.2f, 0.4f, 0.6f, 0.8f)
        val lanes = (0 until GameConfig.LANES).map { index ->
            Lane(
                index = index,
                direction = if (index % 2 == 0) 1 else -1,
                speedFactor = 1.0f,
                vehicles = listOf(
                    VehicleSpec(
                        id = index,
                        kind = if (index % 2 == 0) VehicleKind.TRUCK else VehicleKind.TRACTOR,
                        phase = phases[index]
                    )
                )
            )
        }
        val badges = listOf(
            BadgeCell(1, 0, BadgeTint.DAWN),
            BadgeCell(3, 2, BadgeTint.HARVEST),
            BadgeCell(5, 1, BadgeTint.NIGHT)
        )
        return RoadPlan(seed, lanes, badges, speedMultiplier)
    }
}
