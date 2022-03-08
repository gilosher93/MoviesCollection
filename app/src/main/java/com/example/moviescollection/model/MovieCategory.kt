package com.example.moviescollection.model

data class MovieCategory(
    val categoryType: CategoryType,
    val movies: List<Int>,
    val bigImages: Boolean = false,
    var currentIndex: Int = 1
)