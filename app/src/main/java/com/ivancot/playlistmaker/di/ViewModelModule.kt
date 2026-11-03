package com.ivancot.playlistmaker.di

import com.ivancot.playlistmaker.ui.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get(), get()) }
}
