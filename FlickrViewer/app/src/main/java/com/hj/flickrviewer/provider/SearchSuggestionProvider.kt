package com.hj.flickrviewer.provider

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionProvider: SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.hj.flickrviewer.SearchSuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}