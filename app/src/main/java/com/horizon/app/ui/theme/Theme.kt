package com.horizon.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val HorizonColorScheme = lightColorScheme(
    primary = HorizonBlue,
    onPrimary = Color.White,
    secondary = HorizonOrange,
    onSecondary = Color.White,
    background = HorizonBackground,
    surface = HorizonSurface,
    error = HorizonError,
    onBackground = HorizonTextPrimary,
    onSurface = HorizonTextPrimary,
)

private val HorizonTypography = Typography(
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
)

@Composable
fun HorizonTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HorizonColorScheme,
        typography = HorizonTypography,
        content = content
    )
}
