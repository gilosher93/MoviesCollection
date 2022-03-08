package com.example.moviescollection.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoPreview(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("site") val site: String,
) : Parcelable {
}