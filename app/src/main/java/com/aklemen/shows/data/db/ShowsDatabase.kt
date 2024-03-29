package com.aklemen.shows.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aklemen.shows.data.db.dao.EpisodesDao
import com.aklemen.shows.data.db.dao.ShowsDao
import com.aklemen.shows.data.model.Episode
import com.aklemen.shows.data.model.Show
import com.aklemen.shows.util.ShowsApp

@Database(
    entities = [
        Show::class,
        Episode::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ShowsDatabase : RoomDatabase() {

    abstract fun showsDao(): ShowsDao

    abstract fun episodeDao(): EpisodesDao

    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(): ShowsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        ShowsApp.instance,
                        ShowsDatabase::class.java,
                        "shows_database"
                    )
//                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}