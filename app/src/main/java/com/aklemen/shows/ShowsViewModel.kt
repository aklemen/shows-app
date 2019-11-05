package com.aklemen.shows

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowsViewModel : ViewModel() {

    val showLiveData = MutableLiveData<Show>()
    val imageLiveData = MutableLiveData<Uri>()

    val episodeNumberLiveData = MutableLiveData<EpisodeNumber>()

}

data class EpisodeNumber(
    val season : Int,
    val episode : Int
)