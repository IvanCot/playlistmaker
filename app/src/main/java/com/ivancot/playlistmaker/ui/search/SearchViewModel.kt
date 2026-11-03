package com.ivancot.playlistmaker.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivancot.playlistmaker.domain.api.TracksRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackRepository: TracksRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()
    val query = savedStateHandle.getStateFlow("query", "")
    private var searchJob: Job? = null

    fun onQueryChanged(value: String) {
        if (value == query.value) return
        searchJob?.cancel()
        savedStateHandle["query"] = value
        _searchScreenState.value = SearchState.Initial
    }

    fun searchTracks(expression: String = query.value) {
        searchJob?.cancel()
        if (expression.isBlank()) {
            clearSearch()
            return
        }
        savedStateHandle["query"] = expression
        _searchScreenState.value = SearchState.Searching
        searchJob = viewModelScope.launch {
            try {
                val result = trackRepository.searchTracksContains(expression)
                coroutineContext.ensureActive()
                _searchScreenState.value = if (result.isEmpty()) SearchState.Fail(SearchError.NothingFound)
                    else SearchState.Success(result)
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                coroutineContext.ensureActive()
                _searchScreenState.value = SearchState.Fail(SearchError.Unavailable)
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        savedStateHandle["query"] = ""
        _searchScreenState.value = SearchState.Initial
    }
}
