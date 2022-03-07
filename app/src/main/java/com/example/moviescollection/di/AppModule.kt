package com.example.moviescollection.di

import com.example.moviescollection.network.responses.ConfigResponse
import org.koin.dsl.module

val appModule = module {
    single {
        AppConfig()
    }
}

class AppConfig {
    var config: ConfigResponse? = null

    val baseUrl: String
        get() = config?.images?.baseUrl ?: String()

    val posterSize: String
        get() {
            config?.images?.posterSizes?.let { posterSizes ->
                return posterSizes[4]
            }
            return String()
        }

    val backdropSize: String
        get() {
            config?.images?.backdropSizes?.let { backdropSizes ->
                return backdropSizes[2]
            }
            return String()
        }


}