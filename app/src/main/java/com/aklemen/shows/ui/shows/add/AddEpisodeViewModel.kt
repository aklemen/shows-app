package com.aklemen.shows.ui.shows.add

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


class AddEpisodeViewModel : ViewModel() {

    val imageLiveData = MutableLiveData<Uri>()
    val episodeNumberLiveData = MutableLiveData<EpisodeNumber>()

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    fun addNewEpisode(episode: Episode) {
        RestClient.service.addEpisode(episode)
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
