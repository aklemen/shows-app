package com.aklemen.shows.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode(
    @Json(name = "showId") val showId: String = "",
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "episodeNumber") val episodeNumber: String,
    @Json(name = "season") val season: String?
)

data class EpisodeNumber(
    val season: Int,
    val episode: Int
)