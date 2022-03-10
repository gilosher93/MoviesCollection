package com.example.moviescollection.view_models

import androidx.lifecycle.*
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.results.ApiResult
import com.example.moviescollection.repositories.AppRepository
import com.example.moviescollection.repositories.MoviesRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val moviesRepository: MoviesRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    private val movieMLD = MutableLiveData<ApiResult<MovieDetails>>(ApiResult.Loading)

    fun observeMovie(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<ApiResult<MovieDetails>>
    ) {
        movieMLD.observe(lifecycleOwner, observer)
    }

    fun getMovieById(movieId: Int) {
        viewModelScope.launch {

            // load the thin data (title and image) if already exist
            moviesRepository.getMovie(movieId)?.let {
                movieMLD.value = ApiResult.Success(it)
            }

            moviesRepository.getFullMovie(movieId)?.let { movie ->
                movieMLD.value = ApiResult.Success(movie)
            } ?: run {
                movieMLD.value = ApiResult.Error(Throwable("unable to fetch a movie"))
            }
        }
    }



    fun getBackdropImageUrl(backdropPath: String?) = appRepository.getBackdropImageUrl(backdropPath)

    fun getPosterImageUrl(posterPath: String?) = appRepository.getPosterImageUrl(posterPath)
}