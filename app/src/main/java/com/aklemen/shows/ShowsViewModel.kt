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

    private val _credentialsLiveData = MutableLiveData<Credentials>()
    val credentialsLiveData: LiveData<Credentials> = _credentialsLiveData

    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> = _tokenLiveData

    private val _showListLiveData = MutableLiveData<List<Show>>()
    val showListLiveData: LiveData<List<Show>> = _showListLiveData

    private val _showLiveData = MutableLiveData<Show>()
    val showLiveData: LiveData<Show> = _showLiveData

    private val _episodeListLiveData = MutableLiveData<List<Episode>>()
    val episodeListLiveData: LiveData<List<Episode>> = _episodeListLiveData

    private val _episodeLiveData = MutableLiveData<Episode>()
    val episodeLiveData: LiveData<Episode> = _episodeLiveData


    fun registerUser(credentials: Credentials) {
        Singleton.service.register(credentials)
            .enqueue(object : Callback<DataUser> {
                override fun onFailure(call: Call<DataUser>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _credentialsLiveData.postValue(credentials)
                        } else {
                            _errorLiveData.postValue(IllegalStateException(""))
                        }
                    } else {
                        _errorLiveData.postValue(HttpException(response))
                    }
                }

            })
    }

    fun loginUser(credentials: Credentials) {
        Singleton.service.login(credentials)
            .enqueue(object : Callback<DataToken> {
                override fun onFailure(call: Call<DataToken>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<DataToken>, response: Response<DataToken>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _tokenLiveData.postValue(body.data.token)
                        } else {
                            _errorLiveData.postValue(IllegalStateException(""))
                        }
                    } else {
                        _errorLiveData.postValue(HttpException(response))
                    }
                }

            })
    }

    fun getShowsList(token: String) {
        Singleton.service.getShows(token)
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

    fun getShow(token: String, showId: String) {
        Singleton.service.getShow(token, showId)
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

    fun getEpisodesList(token: String, showId: String) {
        Singleton.service.getEpisodes(token, showId)
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

    fun addNewEpisode(token: String, episode: Episode) {
        Singleton.service.addEpisode(token, episode)
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

data class EpisodeNumber(
    val season: Int,
    val episode: Int
)


interface InfinumApiService {

    @POST("users")
    fun register(@Body credentials: Credentials): Call<DataUser>

    @POST("users/sessions")
    fun login(@Body credentials: Credentials): Call<DataToken>

    @GET("shows")
    fun getShows(@Header("Authorization") token: String): Call<ShowList>

    @GET("shows/{showId}")
    fun getShow(
        @Header("Authorization") token: String,
        @Path("showId") showId: String
    ): Call<DataShow>

    @GET("shows/{showId}/episodes")
    fun getEpisodes(
        @Header("Authorization") token: String,
        @Path("showId") showId: String
    ): Call<EpisodeList>

    @POST("episodes")
    fun addEpisode(@Header("Authorization") token: String, @Body episode: Episode): Call<ResponseBody>

}

