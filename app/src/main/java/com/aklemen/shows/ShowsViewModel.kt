package com.aklemen.shows

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

class ShowsViewModel : ViewModel() {

    val showLiveData = MutableLiveData<Show>()
    val imageLiveData = MutableLiveData<Uri>()

    val episodeNumberLiveData = MutableLiveData<EpisodeNumber>()

    val errorLiveData = MutableLiveData<Throwable>()

}

data class EpisodeNumber(
    val season : Int,
    val episode : Int
)


interface InfinumApiService{

    @POST("users")
    fun register(@Body credentials: Credentials) : Call<User>

    @POST("users/sessions")
    fun login (@Body credentials: Credentials) : Call<ResponseBody>

}