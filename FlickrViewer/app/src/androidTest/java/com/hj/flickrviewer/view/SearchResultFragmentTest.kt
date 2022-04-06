package com.hj.flickrviewer.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.hj.flickrviewer.R
import com.hj.flickrviewer.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SearchResultFragmentTest {
    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun whenDisplayed_searchResult_isDisplayed() {
        launchFragmentInHiltContainer<SearchResultFragment>()

        onView(allOf(withId(R.id.image), isDisplayed()))
        onView(allOf(withId(R.id.title), isDisplayed()))
    }
}