package com.example.moviescollection.di

import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.api.MoviesApi
import org.koin.dsl.module

val moviesModule = module {
    single { MoviesManager(get()) }
}

class MoviesManager(
    private val moviesApi: MoviesApi
) {
    private val moviesMap = mutableMapOf<Int, MovieDetails>()
    private var allMoviesCatalog = listOf<MovieCategory>()

    fun getMovie(id: Int): MovieDetails? = moviesMap[id]

    fun updateMovie(movie: MovieDetails) {
        moviesMap[movie.id] = movie
    }

    fun setMoviesCatalog(moviesCatalog: List<MovieCategory>) {
        allMoviesCatalog = moviesCatalog
    }

    fun getNowPlayingMovies() = getCategoryByIndex(CategoryType.NOW_PLAYING.ordinal)

    fun getPopularMovies() = getCategoryByIndex(CategoryType.POPULAR.ordinal)

    fun getTopRatedMovies() = getCategoryByIndex(CategoryType.TOP_RATED.ordinal)

    fun getUpcomingMovies() = getCategoryByIndex(CategoryType.UPCOMING.ordinal)

    private fun getCategoryByIndex(index: Int): MovieCategory? {
        if (index < allMoviesCatalog.size) {
            return allMoviesCatalog[index]
        }
        return null
    }

    suspend fun loadMoreMovies(categoryType: CategoryType) {
        getCategoryByIndex(categoryType.ordinal)?.let { category ->
            val nextPageIndex = category.currentIndex + 1
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
                val moviesMutableList = category.movies.toMutableList()
                moviesMutableList.addAll(moviesList.map { it.id })

                moviesList.forEach {
                    updateMovie(it)
                }
            }
        }

    }
}