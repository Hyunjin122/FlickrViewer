package com.hj.flickrviewer.model.retrofit

import com.hj.flickrviewer.model.data.FlickrModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("services/rest/")
    suspend fun getPhoto(
        @Query("page") page: Int,
        @Query("text") text: String,
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String = "4fc9da4cfc4a8b2271f503b7bc612b8e",
        @Query("per_page") perPage: String = "25",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: String = "1"
    ): Response<FlickrModel>
}