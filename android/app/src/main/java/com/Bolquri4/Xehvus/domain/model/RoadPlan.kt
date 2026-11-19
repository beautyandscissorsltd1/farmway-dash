package com.Bolquri4.Xehvus.domain.model

import com.Bolquri4.Xehvus.core.config.GameConfig

enum class VehicleKind { TRUCK, TRACTOR }

enum class BadgeTint { DAWN, HARVEST, NIGHT }

data class VehicleSpec(
    val id: Int,
    val kind: VehicleKind,
    val phase: Float
) {
    val halfWidth: Float
        get() = if (kind == VehicleKind.TRUCK) GameConfig.TRUCK_WIDTH / 2f else GameConfig.TRACTOR_WIDTH / 2f
}

data class Lane(
    val index: Int,
    val direction: Int,
    val speedFactor: Float,
    val vehicles: List<VehicleSpec>
)

data class BadgeCell(
    val row: Int,
    val column: Int,
    val tint: BadgeTint
)

data class RoadPlan(
    val seed: Int,
    val lanes: List<Lane>,
    val badges: List<BadgeCell>,
    val speedMultiplier: Float
) {
    val routeId: Int
        get() {
            val raw = seed % 10000
            return if (raw < 0) -raw else raw
        }
}

object RoadMath {

    private const val SPAN = GameConfig.SPAWN_MAX - GameConfig.SPAWN_MIN

    fun columnCenter(column: Int): Float = (column + 0.5f) / GameConfig.COLUMNS

    fun vehicleSpeed(lane: Lane, speedMultiplier: Float): Float =
        GameConfig.BASE_SPEED * lane.speedFactor * speedMultiplier

    fun vehicleX(lane: Lane, vehicle: VehicleSpec, elapsedSeconds: Float, speedMultiplier: Float): Float {
        val speed = vehicleSpeed(lane, speedMultiplier)
        var cycle = vehicle.phase + speed * elapsedSeconds / SPAN
        cycle -= kotlin.math.floor(cycle)
        return if (lane.direction > 0) {
            GameConfig.SPAWN_MIN + cycle * SPAN
        } else {
            GameConfig.SPAWN_MAX - cycle * SPAN
        }
    }

    fun overlaps(vehicleX: Float, vehicle: VehicleSpec, column: Int): Boolean {
        val hen = columnCenter(column)
        val reach = vehicle.halfWidth + GameConfig.HEN_HALF_WIDTH
        val delta = vehicleX - hen
        return (if (delta < 0f) -delta else delta) < reach
    }

    fun laneOccupied(
        plan: RoadPlan,
        laneIndex: Int,
        column: Int,
        elapsedSeconds: Float
    ): Boolean {
        val lane = plan.lanes.getOrNull(laneIndex) ?: return false
        for (vehicle in lane.vehicles) {
            val x = vehicleX(lane, vehicle, elapsedSeconds, plan.speedMultiplier)
            if (overlaps(x, vehicle, column)) return true
        }
        return false
    }

    fun laneSafeFor(
        plan: RoadPlan,
        laneIndex: Int,
        column: Int,
        fromSeconds: Float,
        horizonSeconds: Float
    ): Boolean {
        var t = fromSeconds
        val end = fromSeconds + horizonSeconds
        while (t <= end) {
            if (laneOccupied(plan, laneIndex, column, t)) return false
            t += 0.05f
        }
        return true
    }
}
