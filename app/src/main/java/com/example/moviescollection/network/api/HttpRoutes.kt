package com.example.moviescollection.network.api

object HttpRoutes {
    private const val ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlMDI5ODcxN2QyZWM2MTc3ZDUyYzY4MDk5OTdlNTE0ZSIsInN1YiI6IjYyMjUzYjQzZTMyM2YzMDA0NGQ3ZmU1MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.5dSp0J1xFAveS5jEHO4BDYx1xL7cit3KGassHKBDU4E"

    const val ENGLISH_LANGUAGE = "en-US"
    const val API_KEY = "e0298717d2ec6177d52c6809997e514e"
    const val BASE_URL = "https://api.themoviedb.org/"
    const val API_VERSION = "3"
    const val CONFIGURATION = "$API_VERSION/configuration"
    const val POPULAR_MOVIES = "$API_VERSION/movie/popular"
    const val NOW_PLAYING_MOVIES = "$API_VERSION/movie/now_playing"
    const val TOP_RATED_MOVIES = "$API_VERSION/movie/top_rated"
    const val UPCOMING_MOVIES = "$API_VERSION/movie/upcoming"
    const val GET_MOVIE = "$API_VERSION/movie/{movie_id}"
}