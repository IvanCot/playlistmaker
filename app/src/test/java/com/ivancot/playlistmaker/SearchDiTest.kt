package com.ivancot.playlistmaker

import androidx.lifecycle.SavedStateHandle
import com.ivancot.playlistmaker.data.di.repositoryModule
import com.ivancot.playlistmaker.di.viewModelModule
import com.ivancot.playlistmaker.ui.search.SearchState
import com.ivancot.playlistmaker.ui.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
class SearchDiTest {
    @get:Rule val main = MainDispatcherRule()

    @Test fun resolvesProductionGraphAndSearchesThroughInjectedRepository() = runTest(main.dispatcher) {
        val app = koinApplication {
            modules(repositoryModule, viewModelModule, module { factory { SavedStateHandle() } })
        }
        try {
            val vm = app.koin.get<SearchViewModel>()
            vm.searchTracks("Yesterday")
            advanceUntilIdle()
            val state = vm.searchScreenState.value
            assertTrue(state is SearchState.Success)
            assertEquals("Yesterday (Remastered 2009)", (state as SearchState.Success).foundList.single().trackName)
        } finally { app.close() }
    }
}
