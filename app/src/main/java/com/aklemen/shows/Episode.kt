package com.aklemen.shows

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode (
    @Json(name = "title") val title : String,
    @Json(name = "description") val description : String,
    @Json(name = "episodeNumber") val episodeNumber: EpisodeNumber
)
