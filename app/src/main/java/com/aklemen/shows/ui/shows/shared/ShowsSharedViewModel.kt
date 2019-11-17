package com.aklemen.shows.ui.shows.shared

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class ShowsSharedViewModel : ViewModel() {

    private val _imageLiveData = MutableLiveData<Uri>()
    val imageLiveData: LiveData<Uri> = _imageLiveData

    private val _episodeNumberLiveData = MutableLiveData<EpisodeNumber>()
    val episodeNumberLiveData: LiveData<EpisodeNumber> = _episodeNumberLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    fun setImageUri(uri: Uri?) {
        _imageLiveData.value = uri
    }

    fun setEpisodeNumber(episodeNumber: EpisodeNumber?) {
        _episodeNumberLiveData.value = episodeNumber
    }

}
