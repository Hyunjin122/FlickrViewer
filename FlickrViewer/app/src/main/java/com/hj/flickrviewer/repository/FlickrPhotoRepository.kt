package com.hj.flickrviewer.repository

import com.hj.flickrviewer.model.data.FlickrModel
import retrofit2.Response

interface FlickrPhotoRepository {
    suspend fun getPhoto(page: Int, text: String): Response<FlickrModel>
}