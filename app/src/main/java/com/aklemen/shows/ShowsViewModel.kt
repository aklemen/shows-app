package com.aklemen.shows

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

class ShowsViewModel : ViewModel() {

    val showLiveData = MutableLiveData<Show>()
    val imageLiveData = MutableLiveData<Uri>()

    val episodeNumberLiveData = MutableLiveData<EpisodeNumber>()

    private val _credentialsLiveData = MutableLiveData<Credentials>()
    val credentialsLiveData: LiveData<Credentials> = _credentialsLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData


    fun registerUser(credentials: Credentials) {
        Singleton.service.register(credentials)
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }
                override fun onResponse(call: Call<User>, response: Response<User>) {
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

    fun loginUser(credentials: Credentials){
        Singleton.service.login(credentials)
            .enqueue(object : Callback<Data> {
                override fun onFailure(call: Call<Data>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            val sharedPreferences : SharedPreferences
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
    val season : Int,
    val episode : Int
)


interface InfinumApiService{

    @POST("users")
    fun register(@Body credentials: Credentials) : Call<User>

    @POST("users/sessions")
    fun login (@Body credentials: Credentials) : Call<Data>

}

