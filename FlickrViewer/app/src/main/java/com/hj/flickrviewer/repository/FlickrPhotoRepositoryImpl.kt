package com.hj.flickrviewer.repository

import com.hj.flickrviewer.model.data.FlickrModel
import com.hj.flickrviewer.model.retrofit.FlickrInstance
import retrofit2.Response

class FlickrPhotoRepositoryImpl: FlickrPhotoRepository {
    override suspend fun getPhoto(page: Int, text: String): Response<FlickrModel> {
        return FlickrInstance.api.getPhoto(page, text)
    }
}