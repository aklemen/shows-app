package com.aklemen.shows.data.api

import android.util.Log
import com.aklemen.shows.util.ShowsApp
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RestClient {

    const val BASE_API_URL = "https://api.infinum.academy/api/"
    const val BASE_URL = "https://api.infinum.academy"

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("HttpInterceptor", message)
                }
            })
                .apply{
                    level = HttpLoggingInterceptor.Level.BODY
                }

        )
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val ongoing = chain.request().newBuilder()
                ongoing.addHeader("Accept", "application/json;versions=1")
                val token = ShowsApp.getToken()
                if (token != ""){
                    ongoing.addHeader("Authorization", token)
                }
                return chain.proceed(ongoing.build())
            }

        })
        .build()


    val moshi: Moshi = Moshi.Builder()
        .build()

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: InfinumApiService = retrofit.create(InfinumApiService::class.java)

}