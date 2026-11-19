package com.Bolquri4.Xehvus.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Bolquri4.Xehvus.core.theme.SurfaceDeep
import com.Bolquri4.Xehvus.core.theme.SurfaceWarm
import com.Bolquri4.Xehvus.core.theme.WoodDeep
import com.Bolquri4.Xehvus.core.theme.WoodLight

@Composable
fun WoodPanel(
    modifier: Modifier = Modifier,
    fill: Color = SurfaceWarm,
    corner: Dp = 18.dp,
    elevation: Dp = 10.dp,
    nails: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(corner),
        color = fill,
        border = BorderStroke(1.5.dp, SurfaceDeep),
        shadowElevation = elevation,
        modifier = modifier
    ) {
        Box {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(WoodLight.copy(alpha = 0.5f))
            )
            if (nails) {
                NailHead(Alignment.TopStart)
                NailHead(Alignment.TopEnd)
                NailHead(Alignment.BottomStart)
                NailHead(Alignment.BottomEnd)
            }
            content()
        }
    }
}

@Composable
private fun BoxScope.NailHead(corner: Alignment) {
    Box(
        modifier = Modifier
            .align(corner)
            .padding(7.dp)
            .size(4.dp)
            .background(WoodDeep.copy(alpha = 0.45f), CircleShape)
    )
}
