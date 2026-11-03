package com.ivancot.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.ivancot.playlistmaker.ui.PlaylistBlack
import com.ivancot.playlistmaker.ui.PlaylistBlue
import com.ivancot.playlistmaker.ui.PlaylistMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        setContent {
            PlaylistMakerTheme {
                MainScreen(
                    onSearchClick = { startActivity(Intent(this, SearchActivity::class.java)) },
                    onSettingsClick = { startActivity(Intent(this, SettingsActivity::class.java)) },
                    onLibraryClick = {
                        Toast.makeText(
                            this,
                            getString(R.string.button_pressed, getString(R.string.media_library)),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                )
            }
        }
    }
}

@Composable
private fun MainScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLibraryClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(PlaylistBlue)
            .safeDrawingPadding().padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 14.dp, bottom = 30.dp),
        )
        BoxWithConstraints(Modifier.weight(1f).padding(bottom = 28.dp)) {
            // Equal cards on regular screens; scrolling keeps large text and landscape usable.
            val cardHeight = ((maxHeight - 32.dp) / 3).coerceAtLeast(96.dp)
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                MenuButton(R.string.search, R.drawable.ic_search, cardHeight, onSearchClick)
                MenuButton(R.string.media_library, R.drawable.ic_library, cardHeight, onLibraryClick)
                MenuButton(R.string.settings, R.drawable.ic_settings, cardHeight, onSettingsClick)
            }
        }
    }
}

@Composable
private fun MenuButton(
    @StringRes label: Int,
    @DrawableRes icon: Int,
    height: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = height).semantics { role = Role.Button },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        contentColor = PlaylistBlack,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(label), fontSize = 22.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun MainScreenPreview() {
    PlaylistMakerTheme { MainScreen({}, {}, {}) }
}
