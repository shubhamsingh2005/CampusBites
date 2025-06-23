package com.campusbites.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = PurpleGrey80,
    surface = PurpleGrey80,
    onPrimary = Purple40,
    onSecondary = Purple40,
    onTertiary = Purple40
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = PurpleGrey40,
    surface = PurpleGrey40,
    onPrimary = Purple80,
    onSecondary = Purple80,
    onTertiary = Purple80
)

@Composable
fun CampusbitesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // <-- We will override this using saved preference
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
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
