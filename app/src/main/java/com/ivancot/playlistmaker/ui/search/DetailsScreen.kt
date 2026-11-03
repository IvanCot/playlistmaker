package com.ivancot.playlistmaker.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivancot.playlistmaker.R
import com.ivancot.playlistmaker.domain.model.Track
import com.ivancot.playlistmaker.ui.ScreenHeader

@Composable
fun DetailsScreen(track: Track?, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()) {
        ScreenHeader(R.string.track_details, onBack)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (track == null) Text(stringResource(R.string.track_unavailable))
            else {
                Icon(painterResource(R.drawable.ic_library), null, modifier = Modifier.size(160.dp).padding(32.dp), tint = Color.Gray)
                Text(track.trackName, fontSize = 22.sp)
                Text(track.artistName, fontSize = 16.sp, modifier = Modifier.padding(vertical = 16.dp))
                Text(stringResource(R.string.track_duration, track.trackTime))
            }
        }
    }
}
