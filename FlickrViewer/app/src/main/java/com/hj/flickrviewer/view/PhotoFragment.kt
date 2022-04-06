package com.hj.flickrviewer.view

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.hj.flickrviewer.R
import com.hj.flickrviewer.databinding.FragmentPhotoBinding
import com.hj.flickrviewer.viewmodel.FlickrViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream

class PhotoFragment: Fragment() {
    private val flickrViewModel: FlickrViewModel by activityViewModels()
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private var bookmarkItem: MenuItem? = null
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flickrViewModel.url?.let { url ->
            activity?.let {
                Glide.with(it)
                    .load(url)
                    .into(binding.photoImageView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        flickrViewModel.checkBookmarkStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_photo, menu)
        bookmarkItem = menu.findItem(R.id.bookmark)

        flickrViewModel.bookmarkStatus.observe(viewLifecycleOwner) { bookmarkStatus ->
            bookmarkItem?.let {
                if (bookmarkStatus) it.icon = ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_icon_filled)
                else it.icon = ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_icon_unfilled)
            }
        }

        flickrViewModel.updateBookmarkStatus.observe(viewLifecycleOwner) { bookmarkStatus ->
            bookmarkItem?.let {
                binding.photoImageView.drawable?.let { drawable ->
                    val bitmap = (drawable as BitmapDrawable).bitmap
                    val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val fileName = flickrViewModel.id + ".jpg"
                    storageDir?.let { file ->
                        if (bookmarkStatus) {
                            if (!file.exists()) file.mkdirs()
                            val imageFile = File(file, fileName)
                            if (imageFile.exists()) return@observe
                            else {
                                try {
                                    val stream = FileOutputStream(imageFile)
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                                    stream.close()
                                    snackBar = Snackbar.make(
                                        binding.fragmentPhotoContainer,
                                        "The photo is bookmarked",
                                        Snackbar.LENGTH_SHORT
                                    )
                                    snackBar?.show()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    return@observe
                                }
                            }
                            it.icon = ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_icon_filled)
                        } else {
                            val imageFile = File(file, fileName)
                            imageFile.delete()
                            snackBar = Snackbar.make(
                                binding.fragmentPhotoContainer,
                                "The photo has been removed",
                                Snackbar.LENGTH_SHORT
                            )
                            snackBar?.show()
                            it.icon = ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_icon_unfilled)
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.bookmark) {
            flickrViewModel.updateBookmarkStatus()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        snackBar?.dismiss()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}