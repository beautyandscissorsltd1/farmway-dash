package com.Bolquri4.Xehvus.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.theme.Wood
import com.Bolquri4.Xehvus.core.theme.WoodDeep
import com.Bolquri4.Xehvus.core.ui.PrimaryButton
import com.Bolquri4.Xehvus.core.ui.SecondaryButton

@Composable
fun ControlBar(
    leftHanded: Boolean,
    onLeft: () -> Unit,
    onHop: () -> Unit,
    onRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Wood,
        border = BorderStroke(1.5.dp, WoodDeep),
        shadowElevation = 12.dp,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp)
                .padding(horizontal = 10.dp)
        ) {
            if (leftHanded) {
                SideKey(stringResource(R.string.cta_right), onRight)
                HopKey(onHop)
                SideKey(stringResource(R.string.cta_left), onLeft)
            } else {
                SideKey(stringResource(R.string.cta_left), onLeft)
                HopKey(onHop)
                SideKey(stringResource(R.string.cta_right), onRight)
            }
        }
    }
}

@Composable
private fun RowScope.SideKey(text: String, onClick: () -> Unit) {
    SecondaryButton(
        text = text,
        onClick = onClick,
        height = 64.dp,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun RowScope.HopKey(onClick: () -> Unit) {
    PrimaryButton(
        text = stringResource(R.string.cta_hop),
        onClick = onClick,
        leadingIcon = R.drawable.ic_hop,
        height = 72.dp,
        modifier = Modifier.weight(1.3f)
    )
}
