package com.hj.flickrviewer.viewmodel

import com.hj.flickrviewer.model.data.FlickrModel
import com.hj.flickrviewer.model.data.PhotoModel
import com.hj.flickrviewer.model.data.PhotosModel
import com.hj.flickrviewer.repository.FlickrPhotoRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class FlickrViewModelTest {
    @MockK
    lateinit var photoRepository: FlickrPhotoRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun test_getPhoto_withValidPage_returnDataSetSuccessful() {
        runBlocking {
            coEvery { photoRepository.getPhoto(any(), any()) } returns Response.success(getDefaultFlickrModel())

            val item = photoRepository.getPhoto(1, "test")
            if (item.isSuccessful) {
                assertEquals(item.body()?.photos?.photo?.get(0)?.id, "1")
            }
        }
    }

    private fun getDefaultFlickrModel(): FlickrModel {
        return FlickrModel(
            PhotosModel(
                1,
                1,
                1,
                arrayListOf(PhotoModel("1", "secret", "server", 1, "title"))
            )
        )
    }
}