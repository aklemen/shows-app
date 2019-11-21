package com.aklemen.shows.data.api

import com.aklemen.shows.data.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface InfinumApiService {

    // Login

    @POST("users")
    fun register(@Body credentials: Credentials): Call<DataUser>

    @POST("users/sessions")
    fun login(@Body credentials: Credentials): Call<DataToken>

    // Shows

    @GET("shows")
    fun getShows(): Call<DataShowList>

    @GET("shows/{showId}")
    fun getShow(@Path("showId") showId: String): Call<DataShow>

    // Episodes

    @GET("shows/{showId}/episodes")
    fun getEpisodes(@Path("showId") showId: String): Call<DataEpisodeList>

    @POST("episodes")
    fun addEpisode(@Body episode: EpisodePost): Call<ResponseBody>

    @GET("episodes/{episodeId}")
    fun getEpisode(@Path("episodeId") episodeId: String): Call<DataEpisode>

    // Comments

    @GET("episodes/{episodeId}/comments")
    fun getComments(@Path("episodeId") episodeId: String): Call<DataCommentList>

    @POST("comments")
    fun addComment(@Body comment: Comment): Call<ResponseBody>



}