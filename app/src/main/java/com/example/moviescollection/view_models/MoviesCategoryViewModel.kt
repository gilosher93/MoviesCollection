package com.example.moviescollection.view_models

import androidx.lifecycle.*
import com.example.moviescollection.di.MoviesManager
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.network.api.MoviesApi
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MoviesCategoryViewModel : ViewModel() {

    private val moviesApi: MoviesApi by inject(MoviesApi::class.java)
    private val moviesManager: MoviesManager by inject(MoviesManager::class.java)

    private val moviesCategoryMLD = MutableLiveData<MovieCategory>(null)

    fun observeMoviesCategory(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<MovieCategory>
    ) {
        moviesCategoryMLD.observe(lifecycleOwner, observer)
    }

    fun getMoviesCategory(categoryType: CategoryType) {
        viewModelScope.launch {
            val moviesCategory = when(categoryType) {
                CategoryType.NOW_PLAYING -> {
                    moviesManager.getNowPlayingMovies()
                }
                CategoryType.POPULAR -> {
                    moviesManager.getPopularMovies()
                }
                CategoryType.TOP_RATED -> {
                    moviesManager.getTopRatedMovies()
                }
                CategoryType.UPCOMING -> {
                    moviesManager.getUpcomingMovies()
                }
            }
            moviesCategory?.let {
                moviesCategoryMLD.value = it
            }


//            val movieResponse = moviesApi.getPopularMovies(page = pageIndex)
//            movieResponse.errorBody()?.let {
//                movieMLD.value = MovieDetailsResult.Error(Throwable(it.string().toString()))
//            }
//            movieResponse.body()?.let {
//                moviesManager.updateMovie(it)
//                movieMLD.value = MovieDetailsResult.Success(it)
//            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {

        }
    }
}

sealed class MovieCategoryResult {
    data class Success(val movieCategory: MovieCategory) : MovieCategoryResult()
    data class Error(val throwable: Throwable) : MovieCategoryResult()
    object Loading : MovieCategoryResult()
}