package com.example.moviescollection.network.responses

import com.example.moviescollection.model.Dates
import com.example.moviescollection.model.MovieDetails
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("dates") val dates: Dates,
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieDetails>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)