package com.hj.flickrviewer.model.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FlickrInstance {
    private const val BASE_URL = "https://api.flickr.com/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FlickrApi by lazy {
        retrofit.create(FlickrApi::class.java)
    }
}