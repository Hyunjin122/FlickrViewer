package com.hj.flickrviewer.di

import com.hj.flickrviewer.db.FlickrDao
import com.hj.flickrviewer.repository.FlickrBookmarkRepository
import com.hj.flickrviewer.repository.FlickrBookmarkRepositoryImpl
import com.hj.flickrviewer.repository.FlickrPhotoRepository
import com.hj.flickrviewer.repository.FlickrPhotoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    fun provideFlickrPhotoRepository(): FlickrPhotoRepository {
        return FlickrPhotoRepositoryImpl()
    }

    @Provides
    fun provideFlickrBookmarkRepository(flickrDao: FlickrDao): FlickrBookmarkRepository {
        return FlickrBookmarkRepositoryImpl(flickrDao)
    }
}