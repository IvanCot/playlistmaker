package com.ivancot.playlistmaker.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivancot.playlistmaker.R
import com.ivancot.playlistmaker.domain.model.Track

@Composable
internal fun SearchResults(state: SearchState, onTrackClick: (Track) -> Unit, modifier: Modifier = Modifier) {
    when (state) {
        SearchState.Initial -> InformerState(stringResource(R.string.enter_search_query), modifier = modifier)
        SearchState.Searching -> Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp), trackColor = Color(0xFFE6E8EB))
        }
        is SearchState.Success -> {
            if (state.foundList.isEmpty()) InformerState(stringResource(R.string.no_tracks_found), modifier = modifier)
            else LazyColumn(modifier.fillMaxWidth(), contentPadding = PaddingValues(vertical = 8.dp)) {
                items(state.foundList, key = { it.id }) { track ->
                    TrackListItem(track) { onTrackClick(track) }
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE6E8EB), modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
        is SearchState.Fail -> InformerState(
            title = stringResource(if (state.error == SearchError.NothingFound) R.string.no_tracks_found else R.string.search_failed),
            subtitle = if (state.error == SearchError.Unavailable) stringResource(R.string.search_retry_hint) else null,
            modifier = modifier,
            isError = state.error == SearchError.Unavailable,
        )
    }
}

@Composable
private fun InformerState(title: String, subtitle: String? = null, modifier: Modifier = Modifier, isError: Boolean = false) {
    Column(modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 18.sp, textAlign = TextAlign.Center, color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground)
        if (subtitle != null) Text(subtitle, fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun TrackListItem(track: Track, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painterResource(R.drawable.ic_music), null,
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFFE6E8EB)).padding(10.dp), tint = Color.Gray)
        Column(Modifier.weight(1f).padding(horizontal = 8.dp)) {
            Text(track.trackName, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(track.artistName, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Gray)
        }
        Text(track.trackTime, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
        Icon(painterResource(R.drawable.ic_chevron_right), null, modifier = Modifier.size(24.dp), tint = Color(0xFFAEAFB4))
    }
}
