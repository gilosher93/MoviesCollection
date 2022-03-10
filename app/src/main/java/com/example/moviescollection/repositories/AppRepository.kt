package com.example.moviescollection.repositories

import com.example.moviescollection.network.api.MoviesApi
import com.example.moviescollection.network.responses.ConfigResponse

const val POSTER_SIZE_INDEX = 4
const val BACKDROP_SIZE_INDEX = 2

class AppRepository(
    private val moviesApi: MoviesApi
) {
    private var config: ConfigResponse? = null

    private val baseUrl: String
        get() = config?.images?.baseUrl ?: String()

    private val posterSize: String
        get() {
            config?.images?.posterSizes?.let { posterSizes ->
                if (POSTER_SIZE_INDEX < posterSizes.size) {
                    return posterSizes[POSTER_SIZE_INDEX]
                }
            }
            return String()
        }

    private val backdropSize: String
        get() {
            config?.images?.backdropSizes?.let { backdropSizes ->
                if (BACKDROP_SIZE_INDEX < backdropSizes.size) {
                    return backdropSizes[BACKDROP_SIZE_INDEX]
                }
            }
            return String()
        }

    private val posterPrefixUrl: String
        get() {
            return if (baseUrl.isNotEmpty() && posterSize.isNotEmpty()) {
                baseUrl + posterSize
            } else {
                String()
            }
        }

    private val backdropPrefixUrl: String
        get() {
            return if (baseUrl.isNotEmpty() && backdropSize.isNotEmpty()) {
                baseUrl + backdropSize
            } else {
                String()
            }
        }

    fun getPosterImageUrl(posterPath: String?): String? {
        if (posterPath.isNullOrBlank().not() && posterPrefixUrl.isNotEmpty()) {
            return posterPrefixUrl + posterPath
        }
        return null
    }

    fun getBackdropImageUrl(backdropPath: String?): String? {
        if (backdropPath.isNullOrBlank().not() && backdropPrefixUrl.isNotEmpty()) {
            return backdropPrefixUrl + backdropPath
        }
        return null
    }

    suspend fun getInitialConfig(force: Boolean = false) {
        if (config == null || force) {
            config = moviesApi.getConfig()
        }
    }
}