package com.ivancot.playlistmaker.ui.search

import com.ivancot.playlistmaker.domain.model.Track

sealed class SearchState {
    data object Initial : SearchState()
    data object Searching : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    data class Fail(val error: SearchError) : SearchState()
}

enum class SearchError { NothingFound, Unavailable }
