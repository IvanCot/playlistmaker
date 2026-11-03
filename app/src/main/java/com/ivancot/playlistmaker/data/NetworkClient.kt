package com.ivancot.playlistmaker.data

/** Contract for the data source; currently implemented by a local network simulation. */
interface NetworkClient {
    suspend fun getTracks(): List<TrackDto>
}
