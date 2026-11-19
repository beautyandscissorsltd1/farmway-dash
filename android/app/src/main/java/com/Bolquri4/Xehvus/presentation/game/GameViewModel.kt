package com.Bolquri4.Xehvus.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.core.config.GameConfig
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.data.sample.SampleData
import com.Bolquri4.Xehvus.domain.model.BadgeTint
import com.Bolquri4.Xehvus.domain.model.EndReason
import com.Bolquri4.Xehvus.domain.model.RoadMath
import com.Bolquri4.Xehvus.domain.model.RoadPlan
import com.Bolquri4.Xehvus.domain.model.SessionResult
import com.Bolquri4.Xehvus.domain.model.SessionStats
import com.Bolquri4.Xehvus.domain.model.Stage
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import com.Bolquri4.Xehvus.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val roundEnded = Channel<SessionResult>(Channel.BUFFERED)
    val events: Flow<SessionResult> = roundEnded.receiveAsFlow()

    private var tickJob: Job? = null
    private var bannerJob: Job? = null

    private var stage: Stage = SampleData.stages.first()
    private var plan: RoadPlan? = null
    private var attempt = 0

    private var elapsed = 0f
    private var remainingMs = GameConfig.CROSSING_TIME_MS.toFloat()
    private var hintMs = GameConfig.HINT_VISIBLE_MS.toFloat()

    private var henRow = 0
    private var henColumn = 1
    private var visualRow = 0f
    private var visualColumn = 1f
    private var hopCooldown = 0f
    private var bufferedHop = false
    private var bufferedSide = 0

    private val collectedBadges = HashSet<Int>()
    private val nearMissArmed = HashSet<Int>()
    private val sweepTints = ArrayList<BadgeTint>()

    private var chain = 0
    private var bestChain = 0
    private var score = 0
    private var sessionCorn = 0
    private var badgeTotal = 0
    private var nearMisses = 0
    private var fullSweeps = 0
    private var crossingsCleared = 0
    private var bestStars = 0
    private var bestTimeLeft = 0
    private var newCardId = ""
    private var finished = false
    private var sessionStarted = false
    private var sessionToken = 0L

    fun beginSession(stageIndex: Int, token: Long) {
        if (sessionStarted && sessionToken == token) {
            resume()
            return
        }
        sessionToken = token
        sessionStarted = true
        tickJob?.cancel()
        bannerJob?.cancel()
        while (roundEnded.tryReceive().isSuccess) {
            continue
        }
        stage = SampleData.stageAt(stageIndex)
        attempt = 0
        elapsed = 0f
        remainingMs = GameConfig.CROSSING_TIME_MS.toFloat()
        hintMs = GameConfig.HINT_VISIBLE_MS.toFloat()
        henRow = 0
        henColumn = 1
        visualRow = 0f
        visualColumn = 1f
        hopCooldown = 0f
        bufferedHop = false
        bufferedSide = 0
        collectedBadges.clear()
        nearMissArmed.clear()
        sweepTints.clear()
        chain = 0
        bestChain = 0
        score = 0
        sessionCorn = 0
        badgeTotal = 0
        nearMisses = 0
        fullSweeps = 0
        crossingsCleared = 0
        bestStars = 0
        bestTimeLeft = 0
        newCardId = ""
        finished = false

        val settings = settingsRepository.settings.value
        val progress = progressRepository.progress.value
        _uiState.value = GameUiState(
            stageIndex = stage.index,
            badgesTotal = GameConfig.BADGES_PER_CROSSING,
            leftHanded = settings.leftHanded,
            laneGuides = settings.laneGuides,
            hazardTelegraph = settings.hazardTelegraph,
            reduceMotion = settings.reduceMotion,
            henTint = ServiceLocator.purchaseReward.equippedTint(progress),
            corn = progress.corn
        )
        viewModelScope.launch {
            buildCrossing()
            runLoop()
        }
    }

    fun hop() {
        if (finished || _uiState.value.paused) return
        if (hopCooldown > 0f) {
            bufferedHop = true
            return
        }
        applyHop()
    }

    fun stepLeft() = stepSide(-1)

    fun stepRight() = stepSide(1)

    fun pause() {
        if (finished) return
        _uiState.update { it.copy(paused = true) }
    }

    fun resume() {
        if (finished) return
        _uiState.update { it.copy(paused = false) }
    }

    private fun stepSide(delta: Int) {
        if (finished || _uiState.value.paused) return
        if (hopCooldown > 0f) {
            bufferedSide = delta
            return
        }
        applySide(delta)
    }

    private fun applyHop() {
        val target = (henRow + 1).coerceAtMost(GameConfig.ROWS - 1)
        if (target == henRow) return
        henRow = target
        hopCooldown = GameConfig.HOP_MS / 1000f
        evaluateChain()
        nearMissArmed.clear()
        collectBadge()
        if (henRow == GameConfig.ROWS - 1) clearCrossing()
    }

    private fun applySide(delta: Int) {
        val target = (henColumn + delta).coerceIn(0, GameConfig.COLUMNS - 1)
        if (target == henColumn) return
        henColumn = target
        hopCooldown = GameConfig.SIDE_STEP_MS / 1000f
        nearMissArmed.clear()
        collectBadge()
    }

    private suspend fun buildCrossing() {
        val rush = (1f + GameConfig.RUSH_STEP * crossingsCleared).coerceAtMost(GameConfig.RUSH_CAP)
        val built = withContext(Dispatchers.Default) {
            ServiceLocator.generateRoadPlan(stage, attempt, rush)
        }
        plan = built
        attempt += 1
        elapsed = 0f
        collectedBadges.clear()
        nearMissArmed.clear()
        sweepTints.clear()
        _uiState.update { current ->
            current.copy(
                routeId = built.routeId,
                badges = built.badges.map { BadgeView(it.row, it.column, it.tint) },
                badgesCollected = 0,
                laneDirections = built.lanes.map { it.direction },
                ready = true,
                crossingIndex = crossingsCleared + 1
            )
        }
        publish()
    }

    private fun runLoop() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            var previous = System.nanoTime()
            while (isActive) {
                delay(GameConfig.TICK_MS)
                val now = System.nanoTime()
                val dt = ((now - previous) / 1_000_000_000.0).toFloat().coerceIn(0f, 0.05f)
                previous = now
                if (!_uiState.value.paused && !finished) step(dt)
            }
        }
    }

    private fun step(dt: Float) {
        val current = plan ?: return
        elapsed += dt
        remainingMs -= dt * 1000f
        if (hintMs > 0f) hintMs -= dt * 1000f
        if (hopCooldown > 0f) {
            hopCooldown -= dt
            if (hopCooldown <= 0f) {
                hopCooldown = 0f
                if (bufferedHop) {
                    bufferedHop = false
                    applyHop()
                } else if (bufferedSide != 0) {
                    val pending = bufferedSide
                    bufferedSide = 0
                    applySide(pending)
                }
            }
        }
        visualRow += (henRow - visualRow) * (dt * 11f).coerceAtMost(1f)
        visualColumn += (henColumn - visualColumn) * (dt * 14f).coerceAtMost(1f)
        if (isLane(henRow) && detectCollision(current)) {
            endRound(EndReason.FLATTENED)
            return
        }
        detectNearMiss(current)
        if (remainingMs <= 0f) {
            remainingMs = 0f
            endRound(EndReason.OUT_OF_TIME)
            return
        }
        publish()
    }

    private fun isLane(row: Int): Boolean = row in 1 until GameConfig.ROWS - 1

    private fun detectCollision(current: RoadPlan): Boolean =
        RoadMath.laneOccupied(current, henRow - 1, henColumn, elapsed)

    private fun detectNearMiss(current: RoadPlan) {
        if (!isLane(henRow)) return
        val lane = current.lanes.getOrNull(henRow - 1) ?: return
        val henX = RoadMath.columnCenter(henColumn)
        lane.vehicles.forEach { vehicle ->
            val x = RoadMath.vehicleX(lane, vehicle, elapsed, current.speedMultiplier)
            val raw = x - henX
            val distance = if (raw < 0f) -raw else raw
            val gap = distance - vehicle.halfWidth - GameConfig.HEN_HALF_WIDTH
            val receding = raw * lane.direction > 0f
            if (gap > 0f && gap < GameConfig.NEAR_MISS_GAP && receding) {
                if (nearMissArmed.add(vehicle.id)) {
                    nearMisses += 1
                    sessionCorn += GameConfig.NEAR_MISS_CORN
                }
            } else if (gap > GameConfig.NEAR_MISS_GAP * 3f) {
                nearMissArmed.remove(vehicle.id)
            }
        }
    }

    private fun evaluateChain() {
        val current = plan ?: return
        if (!isLane(henRow)) {
            chain = 0
            return
        }
        val lane = current.lanes.getOrNull(henRow - 1)
        if (lane == null || lane.vehicles.isEmpty()) {
            chain = 0
            return
        }
        val henX = RoadMath.columnCenter(henColumn)
        val speed = RoadMath.vehicleSpeed(lane, current.speedMultiplier)
        var rhythm = false
        lane.vehicles.forEach { vehicle ->
            val x = RoadMath.vehicleX(lane, vehicle, elapsed, current.speedMultiplier)
            val tail = x - lane.direction * vehicle.halfWidth
            val passed = (henX - tail) * -lane.direction
            if (passed > 0f && passed < speed * GameConfig.CHAIN_WINDOW_SEC) rhythm = true
        }
        chain = if (rhythm) chain + 1 else 0
        if (chain > bestChain) bestChain = chain
    }

    private fun collectBadge() {
        val current = plan ?: return
        current.badges.forEachIndexed { index, badge ->
            if (badge.row == henRow && badge.column == henColumn && collectedBadges.add(index)) {
                val multiplier = (1f + chain * GameConfig.CHAIN_STEP)
                    .coerceAtMost(GameConfig.MAX_CHAIN_MULTIPLIER)
                score += (GameConfig.BADGE_SCORE * multiplier).toInt()
                sessionCorn += GameConfig.BADGE_CORN
                badgeTotal += 1
                sweepTints.add(badge.tint)
            }
        }
        if (collectedBadges.size == current.badges.size && current.badges.isNotEmpty()) {
            fullSweeps += 1
            val first = sweepTints.firstOrNull()
            if (first != null && sweepTints.all { it == first }) unlockCard(first)
        }
    }

    private fun unlockCard(tint: BadgeTint) {
        progressRepository.update { value ->
            val outcome = ServiceLocator.unlockCollection(value, tint)
            if (outcome.cardId.isNotEmpty()) newCardId = outcome.cardId
            outcome.progress
        }
    }

    private fun clearCrossing() {
        val current = plan ?: return
        val seconds = (remainingMs / 1000f).toInt().coerceAtLeast(0)
        crossingsCleared += 1
        score += GameConfig.CROSSING_SCORE * crossingsCleared
        score += seconds * GameConfig.TIME_BONUS_PER_SECOND
        if (seconds > bestTimeLeft) bestTimeLeft = seconds
        val stars = ServiceLocator.awardStars.starsFor(
            reachedBarn = true,
            allBadges = collectedBadges.size == current.badges.size,
            bestChain = bestChain
        )
        if (stars > bestStars) bestStars = stars
        henRow = 0
        henColumn = 1
        visualRow = 0f
        visualColumn = 1f
        hopCooldown = 0f
        bufferedHop = false
        bufferedSide = 0
        remainingMs = GameConfig.CROSSING_TIME_MS.toFloat()
        bannerJob?.cancel()
        bannerJob = viewModelScope.launch {
            _uiState.update { it.copy(rushVisible = true) }
            delay(GameConfig.RUSH_BANNER_MS)
            _uiState.update { it.copy(rushVisible = false) }
        }
        viewModelScope.launch { buildCrossing() }
    }

    private fun endRound(reason: EndReason) {
        if (finished) return
        finished = true
        tickJob?.cancel()
        _uiState.update { it.copy(collisionFlash = reason == EndReason.FLATTENED, ready = false) }
        viewModelScope.launch {
            delay(GameConfig.COLLISION_HOLD_MS)
            val result = buildResult(reason)
            persist(result)
            roundEnded.send(result)
        }
    }

    private fun buildResult(reason: EndReason): SessionResult {
        val best = progressRepository.progress.value.bestScore
        return SessionResult(
            stageIndex = stage.index,
            routeId = plan?.routeId ?: 0,
            reason = reason,
            score = score,
            badges = badgeTotal,
            bestChain = bestChain,
            corn = sessionCorn,
            crossingsCleared = crossingsCleared,
            stars = bestStars,
            newCardId = newCardId,
            newBest = score > best
        )
    }

    private fun persist(result: SessionResult) {
        val stats = SessionStats(
            badges = result.badges,
            bestChain = result.bestChain,
            crossings = result.crossingsCleared,
            bestTimeLeft = bestTimeLeft,
            corn = result.corn,
            highestStageCleared = if (result.stars > 0) stage.index else -1,
            nearMisses = nearMisses,
            fullSweeps = fullSweeps
        )
        progressRepository.update { value ->
            val withStars = ServiceLocator.awardStars.apply(
                progress = value,
                stageIndex = stage.index,
                stars = result.stars,
                score = result.score,
                totalStages = SampleData.stages.size
            )
            val withCorn = withStars.copy(corn = withStars.corn + result.corn)
            ServiceLocator.updateMissions.applySession(withCorn, stats)
        }
    }

    private fun publish() {
        val current = plan ?: return
        val vehicles = ArrayList<VehicleView>()
        val telegraph = HashSet<Int>()
        current.lanes.forEach { lane ->
            val speed = RoadMath.vehicleSpeed(lane, current.speedMultiplier)
            lane.vehicles.forEach { vehicle ->
                val x = RoadMath.vehicleX(lane, vehicle, elapsed, current.speedMultiplier)
                vehicles.add(
                    VehicleView(
                        id = vehicle.id,
                        lane = lane.index,
                        kind = vehicle.kind,
                        x = x,
                        widthFraction = vehicle.halfWidth * 2f,
                        flipped = lane.direction < 0
                    )
                )
                if (speed > 0f) {
                    val entry = if (lane.direction > 0) {
                        (-vehicle.halfWidth - x) / speed
                    } else {
                        (x - 1f - vehicle.halfWidth) / speed
                    }
                    if (entry > 0f && entry < GameConfig.TELEGRAPH_SEC) telegraph.add(lane.index)
                }
            }
        }
        val visibleBadges = current.badges.filterIndexed { index, _ -> index !in collectedBadges }
            .map { BadgeView(it.row, it.column, it.tint) }
        val squash = 1f + 0.12f * (henRow - visualRow).let { if (it < 0f) -it else it }
        _uiState.update { state ->
            state.copy(
                remainingSeconds = (remainingMs / 1000f).toInt().coerceAtLeast(0),
                badgesCollected = collectedBadges.size,
                badges = visibleBadges,
                chain = chain,
                score = score,
                corn = progressRepository.progress.value.corn + sessionCorn,
                henRow = henRow,
                henColumn = henColumn,
                visualRow = visualRow,
                visualColumn = visualColumn,
                squash = squash,
                vehicles = vehicles,
                telegraphLanes = telegraph,
                hintVisible = hintMs > 0f,
                crossingIndex = crossingsCleared + 1
            )
        }
    }

    override fun onCleared() {
        tickJob?.cancel()
        bannerJob?.cancel()
        super.onCleared()
    }
}
