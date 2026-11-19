package com.Bolquri4.Xehvus.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.theme.Terracotta
import com.Bolquri4.Xehvus.core.theme.TextOnDark
import com.Bolquri4.Xehvus.core.theme.WoodDeep

@Composable
fun RushBanner(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { full -> -full } + fadeIn(),
        exit = slideOutVertically { full -> -full } + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Terracotta,
            border = BorderStroke(1.5.dp, WoodDeep),
            shadowElevation = 8.dp
        ) {
            Text(
                text = stringResource(R.string.banner_rush),
                style = MaterialTheme.typography.labelLarge,
                color = TextOnDark,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
            )
        }
    }
}
