package com.aklemen.shows

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.*


class ShowsViewModel : ViewModel() {

    val imageLiveData = MutableLiveData<Uri>()
    val episodeNumberLiveData = MutableLiveData<EpisodeNumber>()


    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    private val _showListLiveData = MutableLiveData<List<Show>>()
    val showListLiveData: LiveData<List<Show>> = _showListLiveData

    private val _showLiveData = MutableLiveData<Show>()
    val showLiveData: LiveData<Show> = _showLiveData

    private val _episodeListLiveData = MutableLiveData<List<Episode>>()
    val episodeListLiveData: LiveData<List<Episode>> = _episodeListLiveData

    private val _episodeLiveData = MutableLiveData<Episode>()
    val episodeLiveData: LiveData<Episode> = _episodeLiveData


    fun getShowsList() {
        Singleton.service.getShows()
            .enqueue(object : Callback<ShowList> {
                override fun onFailure(call: Call<ShowList>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _showListLiveData.postValue(body.shows.map {
                                Show(
                                    id = it.id,
                                    title = it.title,
                                    imageUrl = it.imageUrl
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

    fun getShow(showId: String) {
        Singleton.service.getShow(showId)
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
        Singleton.service.getEpisodes(showId)
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

    fun addNewEpisode(episode: Episode) {
        Singleton.service.addEpisode(episode)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

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
