package com.example.moviescollection.di

import com.example.moviescollection.model.MovieDetails
import org.koin.dsl.module

val moviesModule = module {
    single { MoviesManager() }
}

class MoviesManager {
    private val moviesMap = mutableMapOf<Int, MovieDetails>()

    fun getMovie(id: Int): MovieDetails? = moviesMap[id]

    fun updateMovie(movie: MovieDetails) {
        moviesMap[movie.id] = movie
    }
}