package com.hj.flickrviewer.model.data

data class PhotoModel(
    val id: String,
    val secret: String,
    val server: String,
    val farm: Long,
    val title: String
) {
    fun url(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}.jpg"
    }
}
