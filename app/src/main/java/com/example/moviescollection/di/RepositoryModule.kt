package com.example.moviescollection.di

import com.example.moviescollection.repositories.AppRepository
import com.example.moviescollection.repositories.MoviesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        MoviesRepository(get())
    }
    single {
        AppRepository(get())
    }
}