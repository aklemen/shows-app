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
data class Credentials(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)