package com.hj.flickrviewer.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.SearchRecentSuggestions;

public class SearchRecentSuggestionsLimit extends SearchRecentSuggestions {
    private final int limit;
    private final Context context;

    public SearchRecentSuggestionsLimit(Context context, String authority, int mode, int limit) {
        super(context, authority, mode);
        this.limit = limit;
        this.context = context;
    }

    public void limitSearchHistory() {
        truncateHistory(context.getContentResolver(), limit);
    }

    @Override
    protected void truncateHistory(ContentResolver cr, int maxEntries) {
        super.truncateHistory(cr, maxEntries);
    }
}
