package com.ivancot.playlistmaker

import com.ivancot.playlistmaker.data.LocalNetworkClient
import com.ivancot.playlistmaker.data.NetworkClient
import com.ivancot.playlistmaker.data.TracksRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TracksRepositoryTest {
    private val repository = TracksRepositoryImpl(LocalNetworkClient())

    @Test fun filtersTitleIgnoringCaseAndTrimsQuery() = runTest {
        val result = repository.searchTracksContains("  yEsTeRdAy ")
        assertEquals(listOf(1), result.map { it.id })
        assertEquals("02:05", result.single().trackTime)
    }

    @Test fun findsAllEightTracksByArtistAndFormatsDuration() = runTest {
        val start = currentTime
        val result = repository.searchTracksContains("beatles")
        assertEquals(3_000L, currentTime - start)
        assertEquals(8, result.size)
        assertEquals("04:19", result.last().trackTime)
    }

    @Test fun unknownQueryReturnsEmpty() = runTest {
        assertTrue(repository.searchTracksContains("неизвестный трек").isEmpty())
    }

    @Test fun whitespaceDoesNotRequestData() = runTest {
        val client = object : NetworkClient {
            override suspend fun getTracks() = error("Blank input must not reach data source")
        }
        assertTrue(TracksRepositoryImpl(client).searchTracksContains(" \t ").isEmpty())
    }
}
