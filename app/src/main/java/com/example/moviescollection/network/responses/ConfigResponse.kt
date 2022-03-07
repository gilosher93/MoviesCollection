package com.example.moviescollection.network.responses

import com.example.moviescollection.model.Images
import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    @SerializedName("change_keys") val changeKeys: List<String>,
    @SerializedName("images") val images: Images
)