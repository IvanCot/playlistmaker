package com.ivancot.playlistmaker.domain.api

import com.ivancot.playlistmaker.domain.model.Track

interface TracksRepository {
    suspend fun searchTracksContains(expression: String): List<Track>
}
