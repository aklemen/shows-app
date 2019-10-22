package com.aklemen.shows

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Show(
    val id : String,
    val name: String,
    val year : String,
    val description : String,
    var listOfEpisodes : MutableList<Episode>,
    val imgResId : Int
) : Parcelable