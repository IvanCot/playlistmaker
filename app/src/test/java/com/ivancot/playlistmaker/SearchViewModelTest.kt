package com.ivancot.playlistmaker

import androidx.lifecycle.SavedStateHandle
import com.ivancot.playlistmaker.domain.api.TracksRepository
import com.ivancot.playlistmaker.domain.model.Track
import com.ivancot.playlistmaker.ui.search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule val main = MainDispatcherRule()
    private val track = Track(1, "Yesterday", "The Beatles", "02:05")
    private fun repository(block: suspend (String) -> List<Track>) = object : TracksRepository {
        override suspend fun searchTracksContains(expression: String) = block(expression)
    }
    private fun model(block: suspend (String) -> List<Track>) = SearchViewModel(repository(block), SavedStateHandle())

    @Test fun exposesLoadingThenSuccess() = runTest(main.dispatcher) {
        val vm = model { delay(3_000); listOf(track) }
        assertEquals(SearchState.Initial, vm.searchScreenState.value)
        vm.searchTracks("Yesterday")
        assertEquals(SearchState.Searching, vm.searchScreenState.value)
        runCurrent()
        advanceTimeBy(2_999)
        assertEquals(SearchState.Searching, vm.searchScreenState.value)
        advanceUntilIdle()
        assertEquals(SearchState.Success(listOf(track)), vm.searchScreenState.value)
    }

    @Test fun emptyResultAndExceptionHaveDifferentErrors() = runTest(main.dispatcher) {
        val empty = model { emptyList() }
        empty.searchTracks("missing")
        advanceUntilIdle()
        assertEquals(SearchState.Fail(SearchError.NothingFound), empty.searchScreenState.value)
        val failing = model { throw IllegalStateException("test failure") }
        failing.searchTracks("Yesterday")
        advanceUntilIdle()
        assertEquals(SearchState.Fail(SearchError.Unavailable), failing.searchScreenState.value)
    }

    @Test fun clearingCancelsLoadingAndPreventsLateResult() = runTest(main.dispatcher) {
        val vm = model { withContext(NonCancellable) { delay(3_000) }; listOf(track) }
        vm.searchTracks("Yesterday")
        runCurrent()
        vm.clearSearch()
        advanceUntilIdle()
        assertEquals("", vm.query.value)
        assertEquals(SearchState.Initial, vm.searchScreenState.value)
    }

    @Test fun latestRequestWinsEvenWhenOldRepositoryIgnoresCancellation() = runTest(main.dispatcher) {
        val newest = track.copy(id = 2, trackName = "New result")
        val vm = model { query ->
            if (query == "old") { withContext(NonCancellable) { delay(3_000) }; listOf(track) }
            else listOf(newest)
        }
        vm.searchTracks("old")
        runCurrent()
        vm.searchTracks("new")
        advanceUntilIdle()
        assertEquals(SearchState.Success(listOf(newest)), vm.searchScreenState.value)
    }

    @Test fun editingResetsResultsAndBlankQueryNeverSearches() = runTest(main.dispatcher) {
        var requests = 0
        val vm = model { requests++; listOf(track) }
        vm.searchTracks("Yesterday")
        advanceUntilIdle()
        vm.onQueryChanged("Girl")
        assertEquals(SearchState.Initial, vm.searchScreenState.value)
        vm.searchTracks("  ")
        advanceUntilIdle()
        assertEquals(1, requests)
        assertEquals(SearchState.Initial, vm.searchScreenState.value)
    }

    @Test fun restoresQueryFromSavedState() {
        val vm = SearchViewModel(repository { emptyList() }, SavedStateHandle(mapOf("query" to "Beatles")))
        assertEquals("Beatles", vm.query.value)
    }
}
