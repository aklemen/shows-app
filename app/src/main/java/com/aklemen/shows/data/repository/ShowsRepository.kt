package com.aklemen.shows.data.repository

import androidx.lifecycle.LiveData
import com.aklemen.shows.data.db.ShowsDatabase
import com.aklemen.shows.data.model.Show

object ShowsRepository {

    fun getShows() : LiveData<List<Show>> = ShowsDatabase.getDatabase().showsDao().getAll()

}