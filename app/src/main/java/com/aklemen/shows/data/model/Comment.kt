package com.aklemen.shows.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment (
    //TODO Wath out for id to not get response code 400
    @Json(name = "_id") val id: String? = null,
    @Json(name = "text") val text: String,
    @Json(name = "episodeId") val episodeId: String,
    @Json(name = "userEmail") val userEmail: String? = ""
)