package com.aklemen.shows

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.*

class ShowsViewModel : ViewModel() {

    val showLiveData = MutableLiveData<Show>()
    val imageLiveData = MutableLiveData<Uri>()

    val episodeNumberLiveData = MutableLiveData<EpisodeNumber>()

    //---------------------------------------------------------

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    private val _credentialsLiveData = MutableLiveData<Credentials>()
    val credentialsLiveData: LiveData<Credentials> = _credentialsLiveData

    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> = _tokenLiveData

    private val _showsListLiveData = MutableLiveData<List<Show>>()
    val showsListLiveData: LiveData<List<Show>> = _showsListLiveData


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
            .enqueue(object : Callback<ShowsList> {
                override fun onFailure(call: Call<ShowsList>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<ShowsList>, response: Response<ShowsList>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _showsListLiveData.postValue(body.shows.map {
                                Show(
                                    id = it.id,
                                    title = it.title,
                                    imageUrl = it.imageUrl
                                )
                            })
                            Log.d("EVO ME", "TUKI")
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
    fun getShows(@Header("Authorization") token: String): Call<ShowsList>

}

