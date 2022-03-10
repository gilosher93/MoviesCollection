package com.example.moviescollection.network.results

sealed class ApiResult<out T> {
    data class Success<out R>(val data: R) : ApiResult<R>()
    data class Error(val throwable: Throwable) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}