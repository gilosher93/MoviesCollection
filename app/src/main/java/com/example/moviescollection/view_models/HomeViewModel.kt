package com.example.moviescollection.view_models

import android.util.Log
import androidx.lifecycle.*
import com.example.moviescollection.di.AppConfig
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.api.MoviesApi
import com.example.moviescollection.network.responses.MoviesResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel : ViewModel() {

    private val moviesApi: MoviesApi by inject(MoviesApi::class.java)
    private val appConfig: AppConfig by inject(AppConfig::class.java)

    private val allMoviesMLD = MutableLiveData<MovieResult>(MovieResult.Loading)
    private var allMoviesCatalog = listOf<MovieCategory>()
        set(value) {
            field = value
            allMoviesMLD.value = MovieResult.Success(value)
        }

    fun observeAllMovies(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<MovieResult>
    ) {
        allMoviesMLD.observe(lifecycleOwner, observer)
    }

    fun getMoviesCatalog() {
        viewModelScope.launch {
            appConfig.config = moviesApi.getConfig()

            val nowPlaying = async { moviesApi.getNowPlayingMovies() }
            val popular = async { moviesApi.getPopularMovies() }
            val topRated = async { moviesApi.getTopRatedMovies() }
            val upcoming = async { moviesApi.getUpcomingMovies() }

            val allMovies = listOf(
                MovieCategory("Movies Now Playing", nowPlaying.await()?.results ?: listOf(), true),
                MovieCategory("Popular Movies", popular.await()?.results ?: listOf()),
                MovieCategory("Top Rated Movies", topRated.await()?.results ?: listOf()),
                MovieCategory("Upcoming Movies", upcoming.await()?.results ?: listOf()),
            )

            allMoviesCatalog = allMovies
        }
    }
}

sealed class MovieResult {
    data class Success(val allMovies: List<MovieCategory>) : MovieResult()
    data class Error(val throwable: Throwable) : MovieResult()
    object Loading : MovieResult()
}