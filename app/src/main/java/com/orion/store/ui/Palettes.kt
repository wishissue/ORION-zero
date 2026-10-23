package com.orion.store.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

class Palette(val id: String, val label: String, val light: ColorScheme, val dark: ColorScheme, val dusk: ColorScheme, val oled: ColorScheme) {
    fun scheme(theme: String) = when (theme) { "light" -> light; "dusk" -> dusk; "oled" -> oled; else -> dark }
}

/** Pure neutral gray: red = green = blue, so there is never a tint. 0 is black, 255 is white. */
private fun g(v: Int) = Color(v, v, v)

private val MonoLight = lightColorScheme(
    primary = g(0), onPrimary = g(255), primaryContainer = g(232), onPrimaryContainer = g(0),
    inversePrimary = g(235), secondary = g(64), onSecondary = g(255), secondaryContainer = g(236),
    onSecondaryContainer = g(20), tertiary = g(40), onTertiary = g(255), tertiaryContainer = g(224),
    onTertiaryContainer = g(0), background = g(255), onBackground = g(10), surface = g(255),
    onSurface = g(10), surfaceVariant = g(240), onSurfaceVariant = g(80), surfaceTint = g(0),
    inverseSurface = g(10), inverseOnSurface = g(245), error = Color(0xFFB3261E), onError = g(255),
    errorContainer = Color(0xFFF9DEDC), onErrorContainer = Color(0xFF410E0B), outline = g(120), outlineVariant = g(205),
    scrim = g(0), surfaceBright = g(255), surfaceContainer = g(243), surfaceContainerHigh = g(237),
    surfaceContainerHighest = g(230), surfaceContainerLow = g(247), surfaceContainerLowest = g(255), surfaceDim = g(222),
    primaryFixed = g(232), primaryFixedDim = g(210), onPrimaryFixed = g(0), onPrimaryFixedVariant = g(60),
    secondaryFixed = g(232), secondaryFixedDim = g(210), onSecondaryFixed = g(0), onSecondaryFixedVariant = g(60),
    tertiaryFixed = g(232), tertiaryFixedDim = g(210), onTertiaryFixed = g(0), onTertiaryFixedVariant = g(60),
)

private val MonoDark = darkColorScheme(
    primary = g(255), onPrimary = g(0), primaryContainer = g(38), onPrimaryContainer = g(255),
    inversePrimary = g(0), secondary = g(200), onSecondary = g(0), secondaryContainer = g(34),
    onSecondaryContainer = g(235), tertiary = g(224), onTertiary = g(0), tertiaryContainer = g(44),
    onTertiaryContainer = g(255), background = g(10), onBackground = g(240), surface = g(10),
    onSurface = g(240), surfaceVariant = g(30), onSurfaceVariant = g(170), surfaceTint = g(255),
    inverseSurface = g(245), inverseOnSurface = g(20), error = Color(0xFFF2B8B5), onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18), onErrorContainer = Color(0xFFF9DEDC), outline = g(120), outlineVariant = g(55),
    scrim = g(0), surfaceBright = g(44), surfaceContainer = g(20), surfaceContainerHigh = g(27),
    surfaceContainerHighest = g(34), surfaceContainerLow = g(14), surfaceContainerLowest = g(0), surfaceDim = g(10),
    primaryFixed = g(232), primaryFixedDim = g(210), onPrimaryFixed = g(0), onPrimaryFixedVariant = g(60),
    secondaryFixed = g(232), secondaryFixedDim = g(210), onSecondaryFixed = g(0), onSecondaryFixedVariant = g(60),
    tertiaryFixed = g(232), tertiaryFixedDim = g(210), onTertiaryFixed = g(0), onTertiaryFixedVariant = g(60),
)

private val Mono = Palette("mono", "Mono", MonoLight, MonoDark,
    // dusk: a softer, lifted dark gray
    MonoDark.copy(background = g(22), surface = g(22), surfaceDim = g(22), surfaceBright = g(58), surfaceContainerLowest = g(16), surfaceContainerLow = g(28),
        surfaceContainer = g(33), surfaceContainerHigh = g(40), surfaceContainerHighest = g(47), surfaceVariant = g(47), onSurfaceVariant = g(180), outlineVariant = g(70)),
    // oled: true black surfaces
    MonoDark.copy(background = g(0), surface = g(0), surfaceDim = g(0), surfaceContainerLowest = g(0), surfaceContainerLow = g(8),
        surfaceContainer = g(13), surfaceContainerHigh = g(19), surfaceContainerHighest = g(26), surfaceVariant = g(26), surfaceBright = g(36)),
)

val PALETTES = listOf(Mono)
