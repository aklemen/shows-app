package com.aklemen.shows

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Show(
    @Json(name = "_id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String? = "",
    @Json(name = "imageUrl") val imageUrl: String
)