package com.example.moviescollection.view_models

import androidx.lifecycle.*
import com.example.moviescollection.repositories.MoviesRepository
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.network.api.MoviesApi
import kotlinx.coroutines.launch

class MoviesCategoryViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val moviesCategoryMLD = MutableLiveData<MovieCategory>(null)

    fun observeMoviesCategory(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<MovieCategory>
    ) {
        moviesCategoryMLD.observe(lifecycleOwner, observer)
    }

    fun getMoviesCategory(categoryType: CategoryType) {
        viewModelScope.launch {
            moviesRepository.getCategory(categoryType)?.let {
                moviesCategoryMLD.value = it
            }

        }
    }

    fun loadMore() {
        viewModelScope.launch {

        }
    }

    fun getMovie(movieId: Int) = moviesRepository.getMovie(movieId)
}