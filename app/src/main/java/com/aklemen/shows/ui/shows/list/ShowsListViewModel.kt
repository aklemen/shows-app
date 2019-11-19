package com.aklemen.shows.ui.shows.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.api.model.Show
import com.aklemen.shows.data.api.model.ShowList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class ShowsListViewModel : ViewModel() {

    private val _showListLiveData = MutableLiveData<List<Show>>()
    val showListLiveData: LiveData<List<Show>> = _showListLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    fun getShowsList() {
        RestClient.service.getShows()
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
}
