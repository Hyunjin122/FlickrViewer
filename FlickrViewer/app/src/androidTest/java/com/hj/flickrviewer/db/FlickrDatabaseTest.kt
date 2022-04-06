package com.hj.flickrviewer.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FlickrDatabaseTest: TestCase() {
    private lateinit var flickrDao: FlickrDao
    private lateinit var flickrDatabase: FlickrDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        flickrDatabase = FlickrDatabase.getInstance(context, true)
        flickrDao = flickrDatabase.flickrDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        flickrDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_insertFlickrItem_returnFlickrId() {
        runBlocking {
            val item = FlickrEntity("1")
            flickrDao.insert(item)
            println("item count : ${flickrDao.getItemCount()}")
            val flickrTest = flickrDao.getFlickrEntity("1")
            assertEquals(item, flickrTest)
        }
    }

    @Test
    fun test_deleteAll_returnItemCount(){
        flickrDao.deleteAll()
        assertEquals(flickrDao.getItemCount(), 0)
    }
}