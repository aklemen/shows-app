package com.aklemen.shows.ui.shows.episode.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class EpisodeDetailViewModel : ViewModel() {
    private val _episodeLiveData = MutableLiveData<Episode>()
    val episodeLiveData: LiveData<Episode> = _episodeLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    fun getEpisode(episodeId: String) {
        RestClient.service.getEpisode(episodeId)
            .enqueue(object : Callback<DataEpisode> {
                override fun onFailure(call: Call<DataEpisode>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<DataEpisode>, response: Response<DataEpisode>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

                            _episodeLiveData.postValue(body.episode)

                        } else {
                            _errorLiveData.postValue(IllegalStateException(""))
                        }
                    } else {
                        _errorLiveData.postValue(HttpException(response))
                    }
                }
            })
    }

}
