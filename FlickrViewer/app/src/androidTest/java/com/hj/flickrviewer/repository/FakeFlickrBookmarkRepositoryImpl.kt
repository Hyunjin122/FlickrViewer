package com.hj.flickrviewer.repository

class FakeFlickrBookmarkRepositoryImpl : FlickrBookmarkRepository {
    override suspend fun isBookmarked(id: String): Boolean {
        return true
    }

    override suspend fun insertEntity(id: String) {}

    override suspend fun deleteEntity(id: String) {}
}