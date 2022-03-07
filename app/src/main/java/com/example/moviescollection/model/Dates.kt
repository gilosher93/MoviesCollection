package com.example.moviescollection.model

import com.google.gson.annotations.SerializedName

data class Dates(
    @SerializedName("minimum") val minimum: String,
    @SerializedName("maximum") val maximum: String,
)