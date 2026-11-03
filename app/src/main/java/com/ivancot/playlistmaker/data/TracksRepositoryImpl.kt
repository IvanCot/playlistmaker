package com.ivancot.playlistmaker.data

import com.ivancot.playlistmaker.domain.api.TracksRepository
import com.ivancot.playlistmaker.domain.model.Track
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override suspend fun searchTracksContains(expression: String): List<Track> {
        val query = expression.trim()
        if (query.isEmpty()) return emptyList()
        return networkClient.getTracks()
            .filter { it.trackName.contains(query, ignoreCase = true) || it.artistName.contains(query, ignoreCase = true) }
            .map {
                val seconds = it.trackTimeMillis / 1_000
                Track(it.id, it.trackName, it.artistName, String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60))
            }
    }
}
