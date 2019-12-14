package com.aklemen.shows.data.repository

import androidx.lifecycle.LiveData
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.db.ShowsDatabase
import com.aklemen.shows.data.model.Show
import com.aklemen.shows.data.model.DataShowList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

object ShowsRepository {

    private val executor = Executors.newSingleThreadExecutor()

    fun getShows(): LiveData<List<Show>>{
        return ShowsDatabase.getDatabase().showsDao().getAll()
    }

    fun fetchShows(){
        RestClient.service.getShows()
            .enqueue(object : Callback<DataShowList> {
                override fun onFailure(call: Call<DataShowList>, t: Throwable) {
                }

                override fun onResponse(call: Call<DataShowList>, response: Response<DataShowList>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            val listOfShows = body.shows.map {
                                Show(
                                    id = it.id,
                                    title = it.title,
                                    imageUrl = it.imageUrl
                                )
                            }

                            executor.execute{ShowsDatabase.getDatabase().showsDao().insertAll(listOfShows)}

                        } else {
                        }
                    } else {
                    }
                }
            })
    }
}