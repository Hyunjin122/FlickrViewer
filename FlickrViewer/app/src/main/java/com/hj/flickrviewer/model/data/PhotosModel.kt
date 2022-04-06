package com.hj.flickrviewer.model.data

data class PhotosModel(
    val page: Long,
    val pages: Long,
    val total: Long,
    val photo: ArrayList<PhotoModel> = ArrayList()
)
