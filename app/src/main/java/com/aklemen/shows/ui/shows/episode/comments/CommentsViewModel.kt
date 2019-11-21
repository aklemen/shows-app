package com.aklemen.shows.ui.shows.episode.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.model.Comment
import com.aklemen.shows.data.model.DataCommentList
import com.aklemen.shows.data.model.Episode
import com.aklemen.shows.data.model.EpisodePost
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CommentsViewModel : ViewModel() {

    private val _commentListLiveData = MutableLiveData<List<Comment>>()
    val commentListLiveData: LiveData<List<Comment>> = _commentListLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData


    fun getCommentsList(episodeId: String) {
        RestClient.service.getComments(episodeId)
            .enqueue(object : Callback<DataCommentList> {
                override fun onFailure(call: Call<DataCommentList>, t: Throwable) {
                    _errorLiveData.postValue(t)
                }

                override fun onResponse(call: Call<DataCommentList>, response: Response<DataCommentList>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {

                            _commentListLiveData.postValue(body.comments.map {
                                Comment(
                                    id = it.id,
                                    text = it.text,
                                    episodeId = it.episodeId,
                                    userEmail = it.userEmail
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

    fun addNewComment(comment: Comment) {
        RestClient.service.addComment(comment)
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
