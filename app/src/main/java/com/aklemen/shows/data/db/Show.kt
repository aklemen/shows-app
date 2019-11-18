package com.aklemen.shows.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shows")
data class Show(
    // TODO autogenerate id or use existing one?
    @PrimaryKey (autoGenerate = true) val id : Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "likesCount") val likesCount: Int

)