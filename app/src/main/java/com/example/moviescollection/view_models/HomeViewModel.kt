package com.example.moviescollection.view_models

import androidx.lifecycle.*
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.network.results.ApiResult
import com.example.moviescollection.repositories.AppRepository
import com.example.moviescollection.repositories.MoviesRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val appRepository: AppRepository,
    private val moviesManager: MoviesRepository
) : ViewModel() {

    private val allMoviesMLD = MutableLiveData<ApiResult<List<MovieCategory>>>(ApiResult.Loading)

    fun observeAllMovies(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<ApiResult<List<MovieCategory>>>
    ) {
        allMoviesMLD.observe(lifecycleOwner, observer)
    }

    fun getMoviesCatalog() {
        viewModelScope.launch {
            appRepository.getInitialConfig()
            moviesManager.getInitialMoviesCatalog()

            allMoviesMLD.value = ApiResult.Success(moviesManager.allMoviesCatalog)
        }
    }
}