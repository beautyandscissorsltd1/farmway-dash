package com.Bolquri4.Xehvus.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FarmwayLight = lightColorScheme(
    primary = Sage,
    onPrimary = TextOnSage,
    primaryContainer = SageDeep,
    onPrimaryContainer = TextOnDark,
    secondary = Terracotta,
    onSecondary = TextOnDark,
    secondaryContainer = SurfaceAlt,
    onSecondaryContainer = TextPrimary,
    tertiary = BadgeGold,
    onTertiary = TextPrimary,
    background = Cream,
    onBackground = TextPrimary,
    surface = SurfaceWarm,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceDeep,
    outlineVariant = WoodLight,
    error = Danger,
    onError = TextOnDark,
    scrim = DuskDeep
)

private val FarmwayDark = darkColorScheme(
    primary = Sage,
    onPrimary = TextOnSage,
    primaryContainer = SageDeep,
    onPrimaryContainer = TextOnDark,
    secondary = Terracotta,
    onSecondary = TextOnDark,
    secondaryContainer = Wood,
    onSecondaryContainer = TextOnDark,
    tertiary = BadgeGold,
    onTertiary = DuskDeep,
    background = DuskDeep,
    onBackground = TextOnDark,
    surface = RoadEarth,
    onSurface = TextOnDark,
    surfaceVariant = RoadNight,
    onSurfaceVariant = SurfaceDeep,
    outline = Wood,
    outlineVariant = WoodDeep,
    error = Danger,
    onError = TextOnDark,
    scrim = DuskDeep
)

@Composable
fun AppTheme(useDark: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (useDark) FarmwayDark else FarmwayLight,
        typography = AppTypography,
        content = content
    )
}
