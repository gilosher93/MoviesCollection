package com.example.moviescollection.di

import com.example.moviescollection.view_models.HomeViewModel
import com.example.moviescollection.view_models.MovieDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { HomeViewModel() }
    viewModel { MovieDetailsViewModel() }
}