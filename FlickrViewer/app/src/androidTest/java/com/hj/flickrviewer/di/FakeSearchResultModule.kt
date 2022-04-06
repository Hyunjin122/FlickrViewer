package com.hj.flickrviewer.di

import com.hj.flickrviewer.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
@Module
class FakeSearchResultModule {
    @Provides
    fun provideFakeFlickrPhotoRepository(): FlickrPhotoRepository {
        return FakeFlickrPhotoRepositoryImpl()
    }

    @Provides
    fun provideFlickrBookmarkRepository(): FlickrBookmarkRepository {
        return FakeFlickrBookmarkRepositoryImpl()
    }
}