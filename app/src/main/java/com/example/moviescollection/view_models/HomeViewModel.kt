package com.example.moviescollection.view_models

import androidx.lifecycle.*
import com.example.moviescollection.di.AppConfig
import com.example.moviescollection.di.MoviesManager
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.network.api.MoviesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel : ViewModel() {

    private val moviesManager: MoviesManager by inject(MoviesManager::class.java)
    private val moviesApi: MoviesApi by inject(MoviesApi::class.java)
    private val appConfig: AppConfig by inject(AppConfig::class.java)

    private val allMoviesMLD = MutableLiveData<MovieResult>(MovieResult.Loading)

    fun observeAllMovies(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<MovieResult>
    ) {
        allMoviesMLD.observe(lifecycleOwner, observer)
    }

    fun getMoviesCatalog() {
        viewModelScope.launch {
            appConfig.config = moviesApi.getConfig()

            // get all movies data
            val moviesList = listOf(
                async { moviesApi.getNowPlayingMovies() },
                async { moviesApi.getPopularMovies() },
                async { moviesApi.getTopRatedMovies() },
                async { moviesApi.getUpcomingMovies() }
            ).awaitAll()

            // save all movies data
            for (moviesResponse in moviesList.filterNotNull()) {
                moviesResponse.results.forEach {
                    moviesManager.updateMovie(it)
                }
            }

            // model the data by categories
            val allMoviesCatalog = listOf(
                MovieCategory(CategoryType.NOW_PLAYING, moviesList[CategoryType.NOW_PLAYING.ordinal]?.results?.map { it.id } ?: listOf(), true),
                MovieCategory(CategoryType.POPULAR, moviesList[CategoryType.POPULAR.ordinal]?.results?.map { it.id } ?: listOf()),
                MovieCategory(CategoryType.TOP_RATED, moviesList[CategoryType.TOP_RATED.ordinal]?.results?.map { it.id } ?: listOf()),
                MovieCategory(CategoryType.UPCOMING, moviesList[CategoryType.UPCOMING.ordinal]?.results?.map { it.id } ?: listOf()),
            )
            moviesManager.setMoviesCatalog(allMoviesCatalog)
            allMoviesMLD.value = MovieResult.Success(allMoviesCatalog)
        }
    }
}

sealed class MovieResult {
    data class Success(val allMovies: List<MovieCategory>) : MovieResult()
    data class Error(val throwable: Throwable) : MovieResult()
    object Loading : MovieResult()
}