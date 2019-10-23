package com.aklemen.shows


data class Show(
    val id : String,
    val name: String,
    val year : String,
    val description : String,
    var listOfEpisodes : MutableList<Episode>,
    val imgResId : Int
)