package com.aklemen.shows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "shows_table")
data class Show(

    @Json(name = "_id")
    @PrimaryKey
    val id: String,

    @Json(name = "title")
    @ColumnInfo(name = "title")
    val title: String = "",

    @Json(name = "description")
    @ColumnInfo(name = "description")
    val description: String = "",

    @Json(name = "imageUrl")
    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",

    @Json(name = "likesCount")
    @ColumnInfo(name = "likes_count")
    val likesCount: String = "",

    @Json(name = "type")
    @ColumnInfo(name = "type")
    val type: String = ""

)