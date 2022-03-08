package com.example.moviescollection.network.api

import com.example.moviescollection.network.responses.CreditResponse
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.responses.ConfigResponse
import com.example.moviescollection.network.responses.MoviesResponse
import com.example.moviescollection.network.responses.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApi {

    @GET(HttpRoutes.CONFIGURATION)
    suspend fun getConfig(
        @Query("api_key") apikey: String = HttpRoutes.API_KEY
    ): ConfigResponse?

    @GET(HttpRoutes.POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query("api_key") apikey: String = HttpRoutes.API_KEY,
        @Query("language") language: String = HttpRoutes.ENGLISH_LANGUAGE,
        @Query("page") page: Int = 1
    ): MoviesResponse?

    @GET(HttpRoutes.NOW_PLAYING_MOVIES)
    suspend fun getNowPlayingMovies(
        @Query("api_key") apikey: String = HttpRoutes.API_KEY,
        @Query("language") language: String = HttpRoutes.ENGLISH_LANGUAGE,
        @Query("page") page: Int = 1
    ): MoviesResponse?

    @GET(HttpRoutes.TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(
        @Query("api_key") apikey: String = HttpRoutes.API_KEY,
        @Query("language") language: String = HttpRoutes.ENGLISH_LANGUAGE,
        @Query("page") page: Int = 1
    ): MoviesResponse?

    @GET(HttpRoutes.UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(
        @Query("api_key") apikey: String = HttpRoutes.API_KEY,
        @Query("language") language: String = HttpRoutes.ENGLISH_LANGUAGE,
        @Query("page") page: Int = 1
    ): MoviesResponse?

    @GET(HttpRoutes.GET_MOVIE)
    suspend fun getMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") apikey: String = HttpRoutes.API_KEY
    ): Response<MovieDetails?>

    @GET(HttpRoutes.GET_MOVIE_CREDITS)
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: String,
        @Query("api_key") apikey: String = HttpRoutes.API_KEY
    ): Response<CreditResponse?>

    @GET(HttpRoutes.GET_MOVIE_VIDEOS)
    suspend fun getMovieVideoPreviews(
        @Path("movie_id") movieId: String,
        @Query("api_key") apikey: String = HttpRoutes.API_KEY
    ): Response<VideoResponse?>
}