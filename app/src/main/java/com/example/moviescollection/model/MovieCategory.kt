package com.example.moviescollection.model

data class MovieCategory(
    val title: String,
    val movies: List<MovieDetails>,
    val bigImages: Boolean = false
)