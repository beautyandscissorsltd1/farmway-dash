package com.Bolquri4.Xehvus.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.di.ServiceLocator
import com.Bolquri4.Xehvus.core.theme.Cream
import com.Bolquri4.Xehvus.core.theme.Sage
import com.Bolquri4.Xehvus.core.theme.SurfaceAlt
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.TextMuted
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.ui.ChipStat
import com.Bolquri4.Xehvus.core.ui.EmptyState
import com.Bolquri4.Xehvus.core.ui.LockBadge
import com.Bolquri4.Xehvus.core.ui.ScreenScaffold
import com.Bolquri4.Xehvus.core.ui.SectionHeader
import com.Bolquri4.Xehvus.core.ui.cardName
import com.Bolquri4.Xehvus.core.ui.setName

@Composable
fun CollectionScreen(onBack: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = ServiceLocator.factory())
    val state by collectionViewModel.state.collectAsStateWithLifecycle()
    CollectionContent(state = state, onBack = onBack)
}

@Composable
private fun CollectionContent(
    state: CollectionUiState,
    onBack: () -> Unit
) {
    ScreenScaffold(
        title = stringResource(R.string.cta_collection),
        onBack = onBack,
        overlay = listOf(Cream.copy(alpha = 0.80f), Cream.copy(alpha = 0.92f)),
        headerExtra = {
            ChipStat(
                iconRes = R.drawable.ic_card,
                text = stringResource(R.string.collection_counter, state.unlocked, state.total),
                accent = TextPrimary,
                container = SurfaceAlt
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (state.unlocked == 0) {
                EmptyState(
                    iconRes = R.drawable.ic_card,
                    message = stringResource(R.string.collection_empty)
                )
            }
            state.sets.forEach { row ->
                SectionHeader(
                    text = setName(row.set),
                    trailing = if (row.complete) stringResource(R.string.state_complete) else ""
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.cards.forEach { card ->
                        CardTile(card = card)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun RowScope.CardTile(card: CardRow) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceAlt,
        border = BorderStroke(1.5.dp, if (card.unlocked) Sage else SurfaceDeep),
        shadowElevation = if (card.unlocked) 5.dp else 0.dp,
        modifier = Modifier.weight(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .padding(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(48.dp)
            ) {
                Image(
                    painter = painterResource(
                        if (card.id == "grain_truck") R.drawable.sprite_2 else R.drawable.sprite_4
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color(card.tint), BlendMode.Modulate),
                    modifier = Modifier
                        .size(44.dp)
                        .alpha(if (card.unlocked) 1f else 0.18f)
                )
            }
            if (card.unlocked) {
                Text(
                    text = cardName(card.id),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            } else {
                LockBadge(label = stringResource(R.string.state_locked))
                Text(
                    text = stringResource(R.string.collection_hidden),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
