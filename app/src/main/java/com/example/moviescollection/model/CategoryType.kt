package com.example.moviescollection.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CategoryType(val title: String) : Parcelable {
    NOW_PLAYING("Movies Now Playing"),
    POPULAR("Popular Movies"),
    TOP_RATED("Top Rated Movies"),
    UPCOMING("Upcoming Movies"),
}