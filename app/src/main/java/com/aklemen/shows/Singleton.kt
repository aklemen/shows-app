package com.aklemen.shows

import android.util.Log
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Singleton {

    val okHttp = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("HttpInterceptor", message)
                }
            })
        )
        .build()


    val moshi = Moshi.Builder()
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.infinum.academy/api")
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service = retrofit.create(InfinumApiService::class.java)

}