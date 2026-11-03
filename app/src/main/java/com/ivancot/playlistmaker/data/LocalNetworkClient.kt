package com.ivancot.playlistmaker.data

import kotlinx.coroutines.delay

/** No HTTP requests are made at this stage of the course. */
class LocalNetworkClient : NetworkClient {
    override suspend fun getTracks(): List<TrackDto> {
        delay(3_000)
        return tracksDto
    }

    private val tracksDto = listOf(
        TrackDto(1, "Yesterday (Remastered 2009)", "The Beatles", 125_000L),
        TrackDto(2, "Here Comes The Sun (Remastered...)", "The Beatles", 187_000L),
        TrackDto(3, "No Reply", "The Beatles", 152_000L),
        TrackDto(4, "Let It Be", "The Beatles", 243_000L),
        TrackDto(5, "Girl", "The Beatles", 138_000L),
        TrackDto(6, "Michelle", "The Beatles", 167_000L),
        TrackDto(7, "Eleanor Rigby", "The Beatles", 198_000L),
        TrackDto(8, "Come Together", "The Beatles", 259_000L),
    )
}
