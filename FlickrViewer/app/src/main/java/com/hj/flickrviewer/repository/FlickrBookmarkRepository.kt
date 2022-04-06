package com.hj.flickrviewer.repository


interface FlickrBookmarkRepository {
    suspend fun isBookmarked(id: String): Boolean
    suspend fun insertEntity(id: String)
    suspend fun deleteEntity(id: String)
}