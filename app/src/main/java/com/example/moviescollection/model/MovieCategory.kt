package com.example.moviescollection.model

data class MovieCategory(
    val categoryType: CategoryType,
    val movies: MutableList<Int>,
    val bigImages: Boolean = false,
    var currentIndexPage: Int = 1
)