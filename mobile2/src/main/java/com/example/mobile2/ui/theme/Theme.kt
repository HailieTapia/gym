package com.example.mobile2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = OnDark,
    secondary = DarkSecondary,
    onSecondary = OnDark,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = OnDark,
    outline = DarkOutline
)


private val LightColorScheme = lightColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.White,
    secondary = DarkSecondary,
    onSecondary = Color.White,
    tertiary = DarkTertiary,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    onSurface = Color(0xFF000000),
    outline = Color(0xFFBDBDBD)
)


@Composable
fun GymTheme(
    darkTheme: Boolean = true, // Forzado en true para desarrollo
    dynamicColor: Boolean = false, // Desactiva dinámico para usar tus colores
    content: @Composable () -> Unit
)
 {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}