package com.example.moviescollection.network.responses

import android.os.Parcelable
import com.example.moviescollection.model.Cast
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreditResponse(
    @SerializedName("id") val id: String,
    @SerializedName("cast") val cast: List<Cast>
) : Parcelable