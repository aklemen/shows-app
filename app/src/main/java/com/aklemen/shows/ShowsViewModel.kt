package com.aklemen.shows

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowsViewModel : ViewModel() {

    val indexLiveData = MutableLiveData<Int>()
}