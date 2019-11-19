package com.aklemen.shows.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aklemen.shows.data.db.model.Episode
import com.aklemen.shows.data.db.model.Show

@Dao
interface EpisodesDao {

    @Query("SELECT * FROM episodes_table WHERE show_id= :showId")
    fun getAllForShow(showId: Int): LiveData<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(episodes: List<Episode>)

    @Query("SELECT * FROM episodes_table WHERE id =:id")
    fun getById(id: Int): LiveData<Episode>

    @Insert
    fun insert(episode: Episode)

    @Update
    fun update(episode: Episode)
}