package com.example.moviescollection.network

import com.example.moviescollection.network.responses.MoviesResponse

sealed class Result {
    data class Success(val characters: List<MoviesResponse>): Result()
    data class Error(val throwable: Throwable): Result()
    object Loading: Result()
}