package com.example.moviescollection.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetails(
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("budget") val budget: Int,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("status") val status: String?,
    var castList: List<Cast>? = null,
    var videoPreviewList: List<VideoPreview>? = null
) : Parcelable {


//    fun hasFullData() = budget > 0 && runtime > 0 && status != null

    fun hasFullData() = status != null
}