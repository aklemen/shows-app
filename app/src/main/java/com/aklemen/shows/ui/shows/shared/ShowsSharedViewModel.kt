package com.aklemen.shows.ui.shows.shared

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.model.EpisodeNumber
import com.aklemen.shows.util.ShowsApp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File


class ShowsSharedViewModel : ViewModel() {

    private val _imageLiveData = MutableLiveData<Uri>()
    val imageLiveData: LiveData<Uri> = _imageLiveData

    private val _episodeNumberLiveData = MutableLiveData<EpisodeNumber>()
    val episodeNumberLiveData: LiveData<EpisodeNumber> = _episodeNumberLiveData

    private val _errorLiveData = MutableLiveData<Throwable>()
    val errorLiveData: LiveData<Throwable> = _errorLiveData

    fun setImageUri(uri: Uri?) {
        _imageLiveData.value = uri
    }

    fun setEpisodeNumber(episodeNumber: EpisodeNumber?) {
        _episodeNumberLiveData.value = episodeNumber
    }

    fun uploadImage() {
        val imageFile = File(getPath(imageLiveData.value))
        val fileReqBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), imageFile)

        RestClient.service.uploadMedia(fileReqBody)
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

    private fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = ShowsApp.instance.contentResolver.query(uri, projection, null, null, null) ?: return null
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s: String = cursor.getString(columnIndex)
        cursor.close()
        return s
    }

}
