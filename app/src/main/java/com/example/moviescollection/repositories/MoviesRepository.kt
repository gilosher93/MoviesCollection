package com.example.moviescollection.repositories

import android.util.Log
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.api.MoviesApi
import kotlinx.coroutines.*

class MoviesRepository(
    private val moviesApi: MoviesApi
) {
    private val moviesMap = mutableMapOf<Int, MovieDetails>()
    var allMoviesCatalog = listOf<MovieCategory>()

    fun getMovie(id: Int): MovieDetails? = moviesMap[id]

    private fun updateMovie(movie: MovieDetails) {
        moviesMap[movie.id] = movie
    }

    private fun getNowPlayingMovies() = getCategoryByIndex(CategoryType.NOW_PLAYING.ordinal)

    private fun getPopularMovies() = getCategoryByIndex(CategoryType.POPULAR.ordinal)

    private fun getTopRatedMovies() = getCategoryByIndex(CategoryType.TOP_RATED.ordinal)

    private fun getUpcomingMovies() = getCategoryByIndex(CategoryType.UPCOMING.ordinal)

    private fun getCategoryByIndex(index: Int): MovieCategory? {
        if (index < allMoviesCatalog.size) {
            return allMoviesCatalog[index]
        }
        return null
    }

    suspend fun loadMoreMovies(categoryType: CategoryType): List<MovieDetails>? {
        getCategoryByIndex(categoryType.ordinal)?.let { category ->
            val nextPageIndex = category.currentIndexPage + 1
            Log.d("TEST", "category: ${categoryType.name}, load page: $nextPageIndex")
            val moviesListResponse = when (categoryType) {
                CategoryType.NOW_PLAYING -> {
                    moviesApi.getNowPlayingMovies(page = nextPageIndex)
                }
                CategoryType.POPULAR -> {
                    moviesApi.getPopularMovies(page = nextPageIndex)
                }
                CategoryType.TOP_RATED -> {
                    moviesApi.getTopRatedMovies(page = nextPageIndex)
                }
                CategoryType.UPCOMING -> {
                    moviesApi.getUpcomingMovies(page = nextPageIndex)
                }
            }
            moviesListResponse?.results?.let { moviesList ->
                moviesList.forEach {
                    updateMovie(it)
                }

                category.movies.addAll(moviesList.map { it.id })
                category.currentIndexPage++
                return moviesList
            }
        }
        return null
    }

    fun getCategory(categoryType: CategoryType): MovieCategory? {
        return when(categoryType) {
            CategoryType.NOW_PLAYING -> {
                getNowPlayingMovies()
            }
            CategoryType.POPULAR -> {
                getPopularMovies()
            }
            CategoryType.TOP_RATED -> {
                getTopRatedMovies()
            }
            CategoryType.UPCOMING -> {
                getUpcomingMovies()
            }
        }
    }

    suspend fun getInitialMoviesCatalog() = withContext(Dispatchers.IO) {
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
                updateMovie(it)
            }
        }

        // model the data by categories
        allMoviesCatalog = listOf(
            MovieCategory(CategoryType.NOW_PLAYING, moviesList[CategoryType.NOW_PLAYING.ordinal]?.results?.map { it.id }?.toMutableList() ?: mutableListOf(), true),
            MovieCategory(CategoryType.POPULAR, moviesList[CategoryType.POPULAR.ordinal]?.results?.map { it.id }?.toMutableList() ?: mutableListOf()),
            MovieCategory(CategoryType.TOP_RATED, moviesList[CategoryType.TOP_RATED.ordinal]?.results?.map { it.id }?.toMutableList() ?: mutableListOf()),
            MovieCategory(CategoryType.UPCOMING, moviesList[CategoryType.UPCOMING.ordinal]?.results?.map { it.id }?.toMutableList() ?: mutableListOf()),
        )
    }

    suspend fun getFullMovie(id: Int): MovieDetails? {
        val movie = moviesMap[id]
        return if (movie == null || movie.hasFullData().not()) {
            loadFullMovie(id)
        } else {
            movie
        }
    }

    private suspend fun loadFullMovie(movieId: Int) = withContext(Dispatchers.IO) {
        val movieResponse = moviesApi.getMovie(movieId = movieId.toString())
        movieResponse.errorBody()?.let {
            return@withContext null
        }
        movieResponse.body()?.let {
            listOf(
                launch { getCastList(it) },
                launch { getVideoList(it) }
            ).joinAll()

            updateMovie(it)

            return@withContext it
        }
    }

    private suspend fun getCastList(movie: MovieDetails) {
        val response = moviesApi.getMovieCredits(movie.id.toString())
        response.errorBody()?.let {

        }
        response.body()?.let { creditResponse ->
            movie.castList = creditResponse.cast.filter { it.profilePath.isNullOrBlank().not() }
        }
    }

    private suspend fun getVideoList(movie: MovieDetails) {
        val response = moviesApi.getMovieVideoPreviews(movie.id.toString())
        response.errorBody()?.let {

        }
        response.body()?.let { videoResponse ->
            movie.videoPreviewList = videoResponse.videos.filter { it.site.lowercase() == "youtube" }
        }
    }
}