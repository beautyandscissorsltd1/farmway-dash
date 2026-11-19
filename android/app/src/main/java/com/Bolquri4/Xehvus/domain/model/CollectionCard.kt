package com.Bolquri4.Xehvus.domain.model

enum class CardSet { DAWN, HARVEST, NIGHT }

data class CollectionCard(
    val id: String,
    val set: CardSet,
    val tint: BadgeTint
)
