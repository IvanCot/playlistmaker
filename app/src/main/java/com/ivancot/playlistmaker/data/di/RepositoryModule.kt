package com.ivancot.playlistmaker.data.di

import com.ivancot.playlistmaker.data.LocalNetworkClient
import com.ivancot.playlistmaker.data.NetworkClient
import com.ivancot.playlistmaker.data.TracksRepositoryImpl
import com.ivancot.playlistmaker.domain.api.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<NetworkClient> { LocalNetworkClient() }
    factory<TracksRepository> { TracksRepositoryImpl(get()) }
}
