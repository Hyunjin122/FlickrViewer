package com.hj.flickrviewer.view

import android.os.Bundle
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hj.flickrviewer.R
import com.hj.flickrviewer.adapter.SearchResultAdapter
import com.hj.flickrviewer.databinding.FragmentSearchResultBinding
import com.hj.flickrviewer.viewmodel.FlickrViewModel
import com.google.android.material.snackbar.Snackbar
import com.hj.flickrviewer.util.VerticalSpaceItemDecoration

class SearchResultFragment : Fragment() {
    private val flickrViewModel: FlickrViewModel by activityViewModels()
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private var snackBar: Snackbar? = null
    private var isEmptyPage = true

    lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        flickrViewModel.photos.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                isEmptyPage = true
                searchResultAdapter.clearItem()
                binding.noSearchResultText.visibility = View.VISIBLE
            } else {
                isEmptyPage = false
                searchResultAdapter.updateItem(it)
                binding.noSearchResultText.visibility = View.INVISIBLE
            }
        }

        flickrViewModel.nextPhotos.observe(viewLifecycleOwner) {
            searchResultAdapter.addItem(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.menu_search_result, menu)

    override fun onPause() {
        binding.recyclerView.stopScroll()
        snackBar?.dismiss()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            addItemDecoration(VerticalSpaceItemDecoration(60))
            searchResultAdapter = SearchResultAdapter(requireContext())
            adapter = searchResultAdapter
            searchResultAdapter.setOnItemClickListener(object : SearchResultAdapter.OnItemClickListener {
                override fun onItemClick(v: View, id: String, url: String) {
                    flickrViewModel.id = id
                    flickrViewModel.url = url
                    findNavController().navigate(SearchResultFragmentDirections.actionSearchResultFragmentToPhotoFragment())
                }
            })

            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (isEmptyPage) return
                    if (!recyclerView.canScrollVertically(1)) {
                        snackBar = Snackbar.make(requireActivity().findViewById(R.id.activity_main_container), "25 more photos", Snackbar.LENGTH_SHORT).let {
                            val layoutParams = it.view.layoutParams as CoordinatorLayout.LayoutParams
                            layoutParams.anchorId = R.id.bottom_navigation_view
                            layoutParams.gravity = Gravity.TOP
                            it.view.layoutParams = layoutParams
                            it.setAction("Load") { flickrViewModel.addNextPhotos() }
                        }
                        snackBar?.show()
                    } else {
                        snackBar?.dismiss()
                    }
                }
            })
        }
    }
}