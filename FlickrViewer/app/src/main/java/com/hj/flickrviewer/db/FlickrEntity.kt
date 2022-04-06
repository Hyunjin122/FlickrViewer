package com.hj.flickrviewer.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FlickrEntity(
    var flickrId: String
) {
    @PrimaryKey(autoGenerate = true) var index: Int = 0
}