package com.aklemen.shows.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.data.api.model.Credentials
import com.aklemen.shows.data.api.model.DataToken
import com.aklemen.shows.data.api.model.DataUser
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.util.ShowsApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> = _tokenLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData


    fun registerUser(credentials: Credentials) {
        RestClient.service.register(credentials)
            .enqueue(object : Callback<DataUser> {
                override fun onFailure(call: Call<DataUser>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }
                override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

                            loginUser(credentials, false)

                        } else {
                            _errorLiveData.postValue(IllegalStateException(""))
                        }
                    } else {
                        _errorLiveData.postValue(HttpException(response))
                    }
                }

            })
    }

    fun loginUser(credentials : Credentials, rememberMe : Boolean) {
        RestClient.service.login(credentials)
            .enqueue(object : Callback<DataToken> {
                override fun onFailure(call: Call<DataToken>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }
                override fun onResponse(call: Call<DataToken>, response: Response<DataToken>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

                            ShowsApp.setRememberMe(rememberMe)
                            ShowsApp.setToken(body.data.token)
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