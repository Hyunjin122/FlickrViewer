package com.hj.flickrviewer.repository

import com.hj.flickrviewer.db.FlickrDao
import com.hj.flickrviewer.db.FlickrEntity
import javax.inject.Inject

class FlickrBookmarkRepositoryImpl @Inject constructor(
    private val flickrDao: FlickrDao
): FlickrBookmarkRepository {
    override suspend fun isBookmarked(id: String): Boolean {
        return flickrDao.getFlickrEntity(id) != null
    }

    override suspend fun insertEntity(id: String) {
        flickrDao.insert(FlickrEntity(id))
    }

    override suspend fun deleteEntity(id: String) {
        flickrDao.getFlickrEntity(id)?.let {
            flickrDao.delete(it)
        }
    }
}