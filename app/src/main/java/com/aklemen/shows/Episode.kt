package com.aklemen.shows

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Episode (
    val title : String,
    val description : String
) : Parcelable
