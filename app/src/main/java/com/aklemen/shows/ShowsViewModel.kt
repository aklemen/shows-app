package com.aklemen.shows

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowsViewModel : ViewModel() {

    val currentShowLiveData = MutableLiveData<Show>()
    val currentImage = MutableLiveData<Uri>()

}