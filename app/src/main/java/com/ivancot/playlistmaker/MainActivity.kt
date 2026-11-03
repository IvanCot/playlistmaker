package com.ivancot.playlistmaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivancot.playlistmaker.ui.PlaylistBlack
import com.ivancot.playlistmaker.ui.PlaylistBlue
import com.ivancot.playlistmaker.ui.PlaylistMakerTheme
import androidx.navigation.compose.rememberNavController
import com.ivancot.playlistmaker.navigation.PlaylistHost
import com.ivancot.playlistmaker.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
        )
        setContent {
            PlaylistMakerTheme {
                PlaylistHost(rememberNavController(), searchViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPlaylistsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
) {
    Column(Modifier.fillMaxSize().background(PlaylistBlue).statusBarsPadding()) {
        Text(
            text = stringResource(R.string.app_name),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(horizontal = 16.dp, vertical = 14.dp),
        )
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .navigationBarsPadding().verticalScroll(rememberScrollState()),
        ) {
            MenuButton(R.string.search, R.drawable.ic_search, onSearchClick)
            MenuButton(R.string.playlists, R.drawable.ic_library, onPlaylistsClick)
            MenuButton(R.string.favorites, R.drawable.ic_favorite, onFavoritesClick)
            MenuButton(R.string.settings, R.drawable.ic_settings, onSettingsClick)
        }
    }
}

@Composable
private fun MenuButton(@StringRes label: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp).semantics { role = Role.Button },
        color = Color.Transparent,
        contentColor = PlaylistBlack,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(label), fontSize = 22.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Icon(painterResource(R.drawable.ic_chevron_right), contentDescription = null, tint = Color(0xFFAEAFB4), modifier = Modifier.size(24.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MainScreenPreview() {
    PlaylistMakerTheme { MainScreen({}, {}, {}, {}) }
}
