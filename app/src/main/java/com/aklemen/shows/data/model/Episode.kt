package com.aklemen.shows.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(
    tableName = "episodes_table",
    foreignKeys = [
        ForeignKey(
            entity = Show::class,
            parentColumns = ["id"],
            childColumns = ["show_id"],
            onDelete = CASCADE
        )]
)
data class Episode(

    @Json(name = "_id")
    @PrimaryKey
    val id: String,

    @Json(name = "title")
    @ColumnInfo(name = "title")
    val title: String,

    @Json(name = "description")
    @ColumnInfo(name = "description")
    val description: String,

    @Json(name = "imageUrl")
    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",

    @Json(name = "episodeNumber")
    @ColumnInfo(name = "episode_number")
    val episodeNumber: String = "",

    @Json(name = "season")
    @ColumnInfo(name = "season")
    val season: String = "",

    @Json(name = "type")
    @ColumnInfo(name = "type")
    val type: String = "",

    // You have to set it in POST, but when getting a list of shows, it's not required
    @Json(name = "showId")
    @ColumnInfo(name = "show_id")
//    val showId: String?,
    val showId: String = "",

    @Json(name = "mediaId")
    @ColumnInfo(name = "media_id")
    val mediaId: String = ""
)

@JsonClass(generateAdapter = true)
data class EpisodePost(

    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "episodeNumber") val episodeNumber: String = "",
    @Json(name = "season") val season: String = "",
    @Json(name = "showId") val showId: String,
    @Json(name = "mediaId") val mediaId: String = ""
)

data class EpisodeNumber(
    val season: Int,
    val episode: Int
)