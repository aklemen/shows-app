package com.aklemen.shows

data class Show(
    val id : String,
    val name: String,
    val year : String,
    val description : String,
    var listOfEpisodes : List<Episode>,
    val imgResId : Int
)