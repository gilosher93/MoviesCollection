package com.example.moviescollection.view_models

import android.util.Log
import androidx.lifecycle.*
import com.example.moviescollection.di.MoviesManager
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.api.MoviesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
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
            val movie = moviesManager.getMovie(movieId)
            if (movie == null || movie.hasFullData().not()) {
                loadFullMovie(movieId)
            } else {
                movieMLD.value = MovieDetailsResult.Success(movie)
            }
        }
    }

    private suspend fun loadFullMovie(movieId: Int) {
        val movieResponse = moviesApi.getMovie(movieId = movieId.toString())
        movieResponse.errorBody()?.let {
            movieMLD.value = MovieDetailsResult.Error(Throwable(it.string().toString()))
        }
        movieResponse.body()?.let {
            listOf(
                viewModelScope.launch { getCastList(it) },
                viewModelScope.launch { getVideoList(it) }
            ).joinAll()

            Log.d("TEST", "updateMovie: ${it.id}")
            moviesManager.updateMovie(it)
            movieMLD.value = MovieDetailsResult.Success(it)
        }
    }

    private suspend fun getCastList(movie: MovieDetails) {
        Log.d("TEST", "getCastList: ${movie.id}")
        val response = moviesApi.getMovieCredits(movie.id.toString())
        response.errorBody()?.let {

        }
        response.body()?.let { creditResponse ->
            movie.castList = creditResponse.cast.filter { it.profilePath.isNullOrBlank().not() }
        }
    }

    private suspend fun getVideoList(movie: MovieDetails) {
        Log.d("TEST", "getVideoList: ${movie.id}")
        val response = moviesApi.getMovieVideoPreviews(movie.id.toString())
        response.errorBody()?.let {

        }
        response.body()?.let { videoResponse ->
            movie.videoPreviewList = videoResponse.videos.filter { it.site.lowercase() == "youtube" }
        }
    }
}

sealed class MovieDetailsResult {
    data class Success(val movieDetails: MovieDetails) : MovieDetailsResult()
    data class Error(val throwable: Throwable) : MovieDetailsResult()
    object Loading : MovieDetailsResult()
}