package com.aklemen.shows.ui.shows.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.model.DataShow
import com.aklemen.shows.data.model.Episode
import com.aklemen.shows.data.model.EpisodeList
import com.aklemen.shows.data.model.Show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class ShowsDetailViewModel : ViewModel() {

    private val _showLiveData = MutableLiveData<Show>()
    val showLiveData: LiveData<Show> = _showLiveData

    private val _episodeListLiveData = MutableLiveData<List<Episode>>()
    val episodeListLiveData: LiveData<List<Episode>> = _episodeListLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    fun getShow(showId: String) {
        RestClient.service.getShow(showId)
            .enqueue(object : Callback<DataShow> {
                override fun onFailure(call: Call<DataShow>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<DataShow>, response: Response<DataShow>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

                            _showLiveData.postValue(body.show)

                        } else {
                            _errorLiveData.postValue(IllegalStateException(""))
                        }
                    } else {
                        _errorLiveData.postValue(HttpException(response))
                    }
                }
            })
    }

    fun getEpisodesList(showId: String) {
        RestClient.service.getEpisodes(showId)
            .enqueue(object : Callback<EpisodeList> {
                override fun onFailure(call: Call<EpisodeList>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<EpisodeList>, response: Response<EpisodeList>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

                            _episodeListLiveData.postValue(body.episodes.map {
                                Episode(
                                    title = it.title,
                                    description = it.description,
                                    episodeNumber = it.episodeNumber,
                                    season = it.season
                                )
                            })

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
