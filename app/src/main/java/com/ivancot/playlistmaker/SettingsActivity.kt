package com.ivancot.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ivancot.playlistmaker.ui.PlaceholderScreen
import com.ivancot.playlistmaker.ui.PlaylistMakerTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistMakerTheme { PlaceholderScreen(R.string.settings, onBack = ::finish) }
        }
    }
}
