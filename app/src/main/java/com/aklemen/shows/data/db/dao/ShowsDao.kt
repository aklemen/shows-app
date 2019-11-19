package com.aklemen.shows.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aklemen.shows.data.model.Show

@Dao
interface ShowsDao {

    @Query("SELECT * FROM shows_table")
    fun getAll(): LiveData<List<Show>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(shows: List<Show>)

    @Query("SELECT * FROM shows_table WHERE id =:id")
    fun getById(id: String): LiveData<Show>

    @Insert
    fun insert(show: Show)

    @Update
    fun update(show: Show)
}