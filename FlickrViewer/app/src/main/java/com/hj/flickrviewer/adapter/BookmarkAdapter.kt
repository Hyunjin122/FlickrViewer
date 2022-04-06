package com.hj.flickrviewer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hj.flickrviewer.R
import com.hj.flickrviewer.model.data.BookmarkModel

@SuppressLint("NotifyDataSetChanged")
class BookmarkAdapter(private val context: Context): RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {
    var bookmarkPhotos = ArrayList<BookmarkModel>()
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmark_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = bookmarkPhotos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookmarkPhotos[position])
    }

    fun updateItem(bookmarkPhotos: ArrayList<BookmarkModel>) {
        this.bookmarkPhotos = bookmarkPhotos
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, id: String, url: String)
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(item: BookmarkModel) {
            val filePath = item.filePath
            Glide.with(itemView).load(filePath).into(image)
            val dotIndex = item.fileName.lastIndexOf('.')
            val fileName = item.fileName.substring(0, dotIndex)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(image, fileName, filePath)
            }
        }
    }
}