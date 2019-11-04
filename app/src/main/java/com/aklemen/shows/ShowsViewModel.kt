package com.aklemen.shows

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowsViewModel : ViewModel() {

    val currentShowLiveData = MutableLiveData<Show>()
    val list = MutableLiveData<MutableList<Show>>()

}