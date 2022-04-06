package com.hj.flickrviewer.di

import android.content.Context
import com.hj.flickrviewer.db.FlickrDao
import com.hj.flickrviewer.db.FlickrDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideFlickrDatabase(@ApplicationContext context: Context): FlickrDatabase = FlickrDatabase.getInstance(context)

    @Provides
    fun provideFlickrDao(flickrDatabase: FlickrDatabase): FlickrDao = flickrDatabase.flickrDao()
}