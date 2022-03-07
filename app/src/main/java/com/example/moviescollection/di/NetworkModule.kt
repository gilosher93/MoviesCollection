package com.example.moviescollection.di

import com.example.moviescollection.network.api.MoviesApi
import com.example.moviescollection.network.api.HttpRoutes
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        createMoviesApi()
    }
}

fun createMoviesApi(): MoviesApi {
    val builder = OkHttpClient.Builder()
    builder.readTimeout(60, TimeUnit.SECONDS)
    builder.connectTimeout(60, TimeUnit.SECONDS)
    builder.addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Accept", "application/json")
            .method(original.method(), original.body())
            .build()
        chain.proceed(request)
    }

    val client = builder.build()
    val gson = GsonBuilder().setLenient().create()
    val gsonConverterFactory = GsonConverterFactory.create(gson)

    return Retrofit.Builder()
            .baseUrl(HttpRoutes.BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build().create(MoviesApi::class.java)
}
