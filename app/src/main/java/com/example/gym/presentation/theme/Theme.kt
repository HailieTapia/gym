package com.example.gym.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors as WearColors
import androidx.wear.compose.material.MaterialTheme as WearTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background

private val FitnessColors = WearColors(
    primary = Color(0xFFFC5A03), // Fiery Orange for buttons
    onPrimary = Color.Black, // Black text on buttons for contrast
    surface = Color(0xFF121212), // Almost black background for depth
    onSurface = Color(0xFFE0E0E0), // Light gray text/icons on background
    secondary = Color(0xFF06DA01), // Teal for accents
    onSecondary = Color.White // White text on secondary elements
)

@Composable
fun GymTheme(content: @Composable () -> Unit ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FitnessColors.surface)
    ) {
        WearTheme(colors = FitnessColors) {
            content()
        }
    }
}