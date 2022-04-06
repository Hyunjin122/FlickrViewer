package com.hj.flickrviewer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hj.flickrviewer.adapter.BookmarkAdapter
import com.hj.flickrviewer.databinding.FragmentBookmarkBinding
import com.hj.flickrviewer.util.VerticalSpaceItemDecoration
import com.hj.flickrviewer.viewmodel.FlickrViewModel

class BookmarkFragment : Fragment() {
    private val flickrViewModel: FlickrViewModel by activityViewModels()
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        flickrViewModel.bookmarkPhotos.observe(viewLifecycleOwner) {
            bookmarkAdapter.updateItem(it)
        }

        flickrViewModel.getSavedPhotoList(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.bookmarkRecyclerView.apply {
            addItemDecoration(VerticalSpaceItemDecoration(60))
            bookmarkAdapter = BookmarkAdapter(requireContext())
            adapter = bookmarkAdapter
            bookmarkAdapter.setOnItemClickListener(object : BookmarkAdapter.OnItemClickListener {
                override fun onItemClick(v: View, id: String, url: String) {
                    flickrViewModel.id = id
                    flickrViewModel.url = url
                    findNavController().navigate(BookmarkFragmentDirections.actionBookmarkFragmentToPhotoFragment())
                }
            })
        }
    }
}