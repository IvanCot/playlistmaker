package com.ivancot.playlistmaker.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PlaylistBlue = Color(0xFF3772E7)
val PlaylistBlack = Color(0xFF1A1B22)

@Composable
fun PlaylistMakerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PlaylistBlue,
            background = Color.White,
            surface = Color.White,
            onBackground = PlaylistBlack,
            onSurface = PlaylistBlack,
        ),
        content = content,
    )
}
