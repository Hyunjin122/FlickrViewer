package com.hj.flickrviewer.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hj.flickrviewer.model.data.BookmarkModel
import com.hj.flickrviewer.model.data.PhotoModel
import com.hj.flickrviewer.repository.FlickrBookmarkRepository
import com.hj.flickrviewer.repository.FlickrPhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FlickrViewModel @Inject constructor(
    private val flickrPhotoRepository: FlickrPhotoRepository,
    private val flickrBookmarkRepository: FlickrBookmarkRepository,
): ViewModel() {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    val photos: MutableLiveData<ArrayList<PhotoModel>> = MutableLiveData()
    var nextPhotos: MutableLiveData<ArrayList<PhotoModel>> = MutableLiveData()
    val bookmarkPhotos: MutableLiveData<ArrayList<BookmarkModel>> = MutableLiveData()
    val bookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    val updateBookmarkStatus: MutableLiveData<Boolean> = MutableLiveData()
    var url: String? = null
    var id: String? = null

    private var currentPage: Int = 1
    private var term: String? = null

    fun getPhoto(page: Int, text: String, isNext: Boolean = false) {
        viewModelScope.launch {
            try {
                val response = flickrPhotoRepository.getPhoto(page, text)
                if (response.isSuccessful) {
                    currentPage = page
                    term = text
                    if (!isNext) photos.value = response.body()?.photos?.photo
                    else {
                        nextPhotos.value = response.body()?.photos?.photo
                        nextPhotos.value?.clear()
                    }
                } else {
                    Log.e(TAG, "response from API : FAILED")
                }
            } catch (e: Exception) {
                Log.e(TAG, e.printStackTrace().toString())
            }
        }
    }

    fun addNextPhotos() {
        term?.let { getPhoto(currentPage+1, it, true) }
    }

    fun updateBookmarkStatus() {
        id?.let { id ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    if (flickrBookmarkRepository.isBookmarked(id)) {
                        launch {
                            flickrBookmarkRepository.deleteEntity(id)
                        }.join()
                        withContext(Dispatchers.Main) {
                            updateBookmarkStatus.value = false
                        }
                    } else {
                        launch {
                            flickrBookmarkRepository.insertEntity(id)
                        }.join()
                        withContext(Dispatchers.Main) {
                            updateBookmarkStatus.value = true
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.printStackTrace().toString())
                }
            }
        }
    }

    fun checkBookmarkStatus() {
        id?.let { id ->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    val status = flickrBookmarkRepository.isBookmarked(id)
                    withContext(Dispatchers.Main) {
                        bookmarkStatus.value = status
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.printStackTrace().toString())
                }
            }
        }
    }

    fun getSavedPhotoList(context: Context) {
        val fileList = ArrayList<BookmarkModel>()
        viewModelScope.launch {
            val directory = context.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.toString()
            directory?.let {
                val photoFile = File(it)
                val photoFiles = photoFile.listFiles()

                photoFiles?.let {
                    for (file in photoFiles) {
                        val photo = BookmarkModel(file.name, file.path)
                        fileList.add(photo)
                    }
                }
            }
        }

        if (bookmarkPhotos.value != fileList)
            bookmarkPhotos.postValue(fileList)
    }
}