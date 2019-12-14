package com.aklemen.shows.data.api

import com.aklemen.shows.data.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface InfinumApiService {

    @POST("users")
    fun register(@Body credentials: Credentials): Call<DataUser>

    @POST("users/sessions")
    fun login(@Body credentials: Credentials): Call<DataToken>

    @GET("shows")
    fun getShows(): Call<ShowList>

    @GET("shows/{showId}")
    fun getShow(@Path("showId") showId: String): Call<DataShow>

    @GET("shows/{showId}/episodes")
    fun getEpisodes(@Path("showId") showId: String): Call<EpisodeList>

    @POST("episodes")
    fun addEpisode(@Body episode: EpisodePost): Call<ResponseBody>

}