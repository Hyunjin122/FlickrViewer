package com.hj.flickrviewer.repository

import com.hj.flickrviewer.model.data.FlickrModel
import com.hj.flickrviewer.model.data.PhotoModel
import com.hj.flickrviewer.model.data.PhotosModel
import retrofit2.Response

class FakeFlickrPhotoRepositoryImpl: FlickrPhotoRepository {
    override suspend fun getPhoto(page: Int, text: String): Response<FlickrModel> {
        return Response.success(FlickrModel(
            PhotosModel(
                1,
                1,
                1,
                arrayListOf(PhotoModel("1", "secret", "server", 1, "title"))
            )
        ))
    }
}