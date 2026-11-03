package com.ivancot.playlistmaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivancot.playlistmaker.R
import com.ivancot.playlistmaker.domain.model.Track
import com.ivancot.playlistmaker.ui.search.SearchViewModel
import com.ivancot.playlistmaker.ui.search.SearchResults

@Composable
fun SearchScreen(searchViewModel: SearchViewModel, navigateToDetailScreen: (Track) -> Unit, onBack: () -> Unit) {
    val query by searchViewModel.query.collectAsStateWithLifecycle()
    val screenState by searchViewModel.searchScreenState.collectAsStateWithLifecycle()
    val focus = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val description = stringResource(R.string.search_query)
    fun search() {
        if (query.isNotBlank()) {
            searchViewModel.searchTracks()
            keyboard?.hide()
            focus.clearFocus()
        }
    }
    Column(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding().imePadding()) {
        ScreenHeader(R.string.search, onBack)
        BasicTextField(
            value = query,
            onValueChange = searchViewModel::onQueryChanged,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth().heightIn(min = 48.dp)
                .background(Color(0xFFE6E8EB), RoundedCornerShape(8.dp))
                .semantics { contentDescription = description },
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, color = PlaylistBlack),
            cursorBrush = SolidColor(PlaylistBlue),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { search() }),
            decorationBox = { input ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = ::search, enabled = query.isNotBlank()) {
                        Icon(painterResource(R.drawable.ic_search), stringResource(R.string.perform_search), tint = Color(0xFFAEAFB4), modifier = Modifier.size(20.dp))
                    }
                    Box(Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 12.dp)) {
                        if (query.isEmpty()) Text(stringResource(R.string.search), fontSize = 16.sp, color = Color(0xFFAEAFB4))
                        input()
                    }
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { searchViewModel.clearSearch(); keyboard?.hide(); focus.clearFocus() }) {
                            Icon(painterResource(R.drawable.ic_clear), stringResource(R.string.clear_query), tint = Color(0xFFAEAFB4))
                        }
                    }
                }
            },
        )
        SearchResults(screenState, navigateToDetailScreen, Modifier.weight(1f))
    }
}
