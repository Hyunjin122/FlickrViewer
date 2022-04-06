package com.hj.flickrviewer.view

import android.annotation.SuppressLint
import android.app.SearchManager
import android.app.SearchableInfo
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.*
import android.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hj.flickrviewer.R
import com.hj.flickrviewer.databinding.FragmentSearchBinding
import com.hj.flickrviewer.provider.SearchSuggestionProvider
import com.hj.flickrviewer.viewmodel.FlickrViewModel
import com.google.android.material.snackbar.Snackbar
import com.hj.flickrviewer.util.NetworkConnection
import com.hj.flickrviewer.util.SearchRecentSuggestionsLimit


class SearchFragment : Fragment() {
    private val flickrViewModel: FlickrViewModel by activityViewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchView: SearchView? = null

    lateinit var searchableInfo: SearchableInfo
    lateinit var searchRecentSuggestionsLimit: SearchRecentSuggestionsLimit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        searchView?.isIconified = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if(!NetworkConnection().isInternetConnected(activity)) {
            val snackBar = Snackbar.make(binding.root, "Check Network status", Snackbar.LENGTH_SHORT)
            val layoutParams = snackBar.view.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.gravity = Gravity.CENTER
            snackBar.view.layoutParams = layoutParams
            snackBar.show()
            return
        }

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)

        searchView = menu.findItem(R.id.search_view).actionView as SearchView
        searchView?.apply {
            searchableInfo = (activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager).getSearchableInfo(activity?.componentName)
            setSearchableInfo(searchableInfo)
            searchRecentSuggestionsLimit =
                SearchRecentSuggestionsLimit(
                    activity,
                    SearchSuggestionProvider.AUTHORITY,
                    SearchSuggestionProvider.MODE,
                    7
                )
            isSubmitButtonEnabled = true

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    activity?.let {
                        SearchRecentSuggestions(it, SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
                            .saveRecentQuery(query, null)
                        flickrViewModel.getPhoto(1, query)
                        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment())
                        searchView?.clearFocus()
                    }

                    searchRecentSuggestionsLimit.limitSearchHistory()
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return true
                }
            })

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return true
                }

                @SuppressLint("Range")
                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = getRecentSuggestions()
                    cursor?.moveToPosition(position)
                    val query = cursor?.getString(cursor.getColumnIndex("suggest_text_1"))
                    query?.let { flickrViewModel.getPhoto(1, it) }
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment())
                    return true
                }
            })

            setOnCloseListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        searchView?.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getRecentSuggestions(): Cursor? {
        val uriBuilder = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(SearchSuggestionProvider.AUTHORITY)

        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY)

        val selection = searchableInfo.suggestSelection
        val selArgs = arrayOf("")

        val uri = uriBuilder.build()
        return activity?.contentResolver?.query(uri, null, selection, selArgs, null)
    }
}