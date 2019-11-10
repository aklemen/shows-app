package com.aklemen.shows

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "_id") val id : String,
    @Json(name = "email") val email : String,
    @Json(name = "type") val type : String
)

@JsonClass(generateAdapter = true)
data class DataUser (
    @Json(name = "data") val data: User
)

@JsonClass(generateAdapter = true)
data class Credentials(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class DataToken (
    @Json(name = "data") val data: Token
)


@JsonClass(generateAdapter = true)
data class Token (
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class ShowsList(
    @Json(name = "data") val shows : List<Show>
)