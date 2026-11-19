package com.Bolquri4.Xehvus.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.data.sample.SampleData
import com.Bolquri4.Xehvus.domain.model.CardSet
import com.Bolquri4.Xehvus.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CardRow(
    val id: String,
    val unlocked: Boolean,
    val tint: Long
)

data class CardSetRow(
    val set: CardSet,
    val cards: List<CardRow>,
    val complete: Boolean
)

data class CollectionUiState(
    val sets: List<CardSetRow> = emptyList(),
    val unlocked: Int = 0,
    val total: Int = 0
)

class CollectionViewModel(
    progress: ProgressRepository
) : ViewModel() {

    val state: StateFlow<CollectionUiState> = progress.progress
        .map { value ->
            val sets = CardSet.entries.map { set ->
                CardSetRow(
                    set = set,
                    cards = SampleData.cardsOf(set).map { card ->
                        CardRow(
                            id = card.id,
                            unlocked = card.id in value.unlockedCards,
                            tint = tintOf(set)
                        )
                    },
                    complete = ServiceLocator.unlockCollection.isSetComplete(value, set)
                )
            }
            CollectionUiState(
                sets = sets,
                unlocked = value.unlockedCards.size,
                total = SampleData.cards.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CollectionUiState(total = SampleData.cards.size)
        )

    private fun tintOf(set: CardSet): Long = when (set) {
        CardSet.DAWN -> 0xFFD9A441
        CardSet.HARVEST -> 0xFF7FA46B
        CardSet.NIGHT -> 0xFF5B6C8C
    }
}
