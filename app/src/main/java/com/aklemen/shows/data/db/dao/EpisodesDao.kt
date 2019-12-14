package com.aklemen.shows.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aklemen.shows.data.model.Episode

@Dao
interface EpisodesDao {

    @Query("SELECT * FROM episodes_table WHERE show_id= :showId")
    fun getAllForShow(showId: String): LiveData<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(episodes: List<Episode>)

    @Query("SELECT * FROM episodes_table WHERE id =:id")
    fun getById(id: String): LiveData<Episode>

    @Insert
    fun insert(episode: Episode)

    @Update
    fun update(episode: Episode)
}