package com.example.moviescollection.network.responses

import android.os.Parcelable
import com.example.moviescollection.model.VideoPreview
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoResponse(
    @SerializedName("id") val id: String,
    @SerializedName("results") val videos: List<VideoPreview>
) : Parcelable