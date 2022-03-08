package com.example.moviescollection.view_models

import androidx.lifecycle.*
import com.example.moviescollection.di.AppConfig
import com.example.moviescollection.di.MoviesManager
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.api.MoviesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MovieDetailsViewModel : ViewModel() {

    private val moviesApi: MoviesApi by inject(MoviesApi::class.java)
    private val moviesManager: MoviesManager by inject(MoviesManager::class.java)

    private val movieMLD = MutableLiveData<MovieDetailsResult>(MovieDetailsResult.Loading)

    fun observeMovie(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<MovieDetailsResult>
    ) {
        movieMLD.observe(lifecycleOwner, observer)
    }

    fun getMovieById(movieId: Int) {
        viewModelScope.launch {
            moviesManager.getMovie(movieId)?.let {
                movieMLD.value = MovieDetailsResult.Success(it)
            } ?: run {
                delay(2000)
                val movieResponse = moviesApi.getMovie(movieId = movieId.toString())
                movieResponse.errorBody()?.let {
                    movieMLD.value = MovieDetailsResult.Error(Throwable(it.string().toString()))
                }
                movieResponse.body()?.let {
                    moviesManager.updateMovie(it)
                    movieMLD.value = MovieDetailsResult.Success(it)
                }
            }
        }
    }
}

sealed class MovieDetailsResult {
    data class Success(val movieDetails: MovieDetails) : MovieDetailsResult()
    data class Error(val throwable: Throwable) : MovieDetailsResult()
    object Loading : MovieDetailsResult()
}