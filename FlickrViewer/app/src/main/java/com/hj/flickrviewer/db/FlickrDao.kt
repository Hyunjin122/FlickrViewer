package com.hj.flickrviewer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlickrDao {
    @Query("SELECT * FROM FlickrEntity WHERE flickrId=:id")
    fun getFlickrEntity(id: String): FlickrEntity?

    @Insert
    fun insert(id: FlickrEntity)

    @Delete
    fun delete(id: FlickrEntity)

    @Query("DELETE FROM FlickrEntity")
    fun deleteAll()

    @Query("SELECT count(*) FROM FlickrEntity")
    fun getItemCount(): Int
}