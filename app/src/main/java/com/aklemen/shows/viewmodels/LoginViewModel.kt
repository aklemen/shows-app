package com.aklemen.shows.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.models.Credentials
import com.aklemen.shows.models.DataToken
import com.aklemen.shows.models.DataUser
import com.aklemen.shows.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LoginViewModel : ViewModel(){

    private val _credentialsLiveData = MutableLiveData<Credentials>()
    val credentialsLiveData: LiveData<Credentials> = _credentialsLiveData

    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> = _tokenLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData


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

}