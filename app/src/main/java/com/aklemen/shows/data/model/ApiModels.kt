package com.aklemen.shows.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.synthetic.main.fragment_add_episode.*

// Extra models

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "_id") val id: String,
    @Json(name = "email") val email: String,
    @Json(name = "type") val type: String
)

@JsonClass(generateAdapter = true)
data class Credentials(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "token") val token: String
)

// Data models

@JsonClass(generateAdapter = true)
data class DataUser(
    @Json(name = "data") val data: User
)

@JsonClass(generateAdapter = true)
data class DataToken(
    @Json(name = "data") val data: Token
)

@JsonClass(generateAdapter = true)
data class DataShowList(
    @Json(name = "data") val shows: List<Show>
)

@JsonClass(generateAdapter = true)
data class DataEpisodeList(
    @Json(name = "data") val episodes: List<Episode>
)

@JsonClass(generateAdapter = true)
data class DataShow(
    @Json(name = "data") val show: Show
)

@JsonClass(generateAdapter = true)
data class DataEpisode(
    @Json(name = "data") val episode: Episode
)

@JsonClass(generateAdapter = true)
data class DataCommentList(
    @Json(name = "data") val comments: List<Comment>
)

