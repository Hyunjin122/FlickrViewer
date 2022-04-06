package com.hj.flickrviewer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [FlickrEntity::class], version = 1)
abstract class FlickrDatabase: RoomDatabase() {
    abstract fun flickrDao(): FlickrDao

    companion object {
        private const val DATABASE_NAME = "flickr_id.db"

        @Volatile
        private var instance: FlickrDatabase? = null

        fun getInstance(appContext: Context, testMode: Boolean = false): FlickrDatabase =
            instance ?: synchronized(this) {
                if (testMode) instance ?: buildTestDatabase(appContext).also { instance = it }
                else instance ?: buildDatabase(appContext).also { instance = it }
            }

        private fun buildDatabase(@ApplicationContext context: Context) =
            Room.databaseBuilder(context, FlickrDatabase::class.java, DATABASE_NAME)
                .build()

        private fun buildTestDatabase(@ApplicationContext context: Context) =
            Room.inMemoryDatabaseBuilder(context, FlickrDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }
}