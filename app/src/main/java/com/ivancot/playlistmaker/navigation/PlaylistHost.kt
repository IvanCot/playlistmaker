package com.ivancot.playlistmaker.navigation

import android.graphics.Color
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ivancot.playlistmaker.MainScreen
import com.ivancot.playlistmaker.R
import com.ivancot.playlistmaker.ui.SearchScreen
import com.ivancot.playlistmaker.ui.SettingsScreen

enum class PlaylistScreen { Main, Search, Settings }

@Composable
fun PlaylistHost(navController: NavHostController) {
    val context = LocalContext.current
    val activity = LocalActivity.current as? ComponentActivity
    val entry by navController.currentBackStackEntryAsState()
    val isMain = entry?.destination?.route == PlaylistScreen.Main.name
    SideEffect {
        activity?.enableEdgeToEdge(
            statusBarStyle = if (isMain) SystemBarStyle.dark(Color.TRANSPARENT)
                else SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
    }
    fun open(screen: PlaylistScreen) {
        navController.navigate(screen.name) { launchSingleTop = true }
    }
    fun back() { navController.popBackStack() }
    fun showToast(label: Int) {
        Toast.makeText(context, context.getString(R.string.button_pressed, context.getString(label)), Toast.LENGTH_SHORT).show()
    }
    NavHost(navController, startDestination = PlaylistScreen.Main.name) {
        composable(PlaylistScreen.Main.name) {
            MainScreen(
                onSearchClick = { open(PlaylistScreen.Search) },
                onSettingsClick = { open(PlaylistScreen.Settings) },
                onPlaylistsClick = { showToast(R.string.playlists) },
                onFavoritesClick = { showToast(R.string.favorites) },
            )
        }
        composable(PlaylistScreen.Search.name) { SearchScreen(onBack = ::back) }
        composable(PlaylistScreen.Settings.name) { SettingsScreen(onBack = ::back) }
    }
}
