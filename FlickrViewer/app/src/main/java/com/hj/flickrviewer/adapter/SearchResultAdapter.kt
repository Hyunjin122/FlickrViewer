package com.hj.flickrviewer.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hj.flickrviewer.R
import com.hj.flickrviewer.model.data.PhotoModel

@SuppressLint("NotifyDataSetChanged")
class SearchResultAdapter(private val context: Context): RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    var photos = ArrayList<PhotoModel>()
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    fun updateItem(photos: ArrayList<PhotoModel>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    fun addItem(photos: ArrayList<PhotoModel>) {
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }

    fun clearItem() {
        this.photos.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(v:View, id: String, url: String)
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)

        fun bind(item: PhotoModel) {
            title.text = item.title
            val url = item.url()
            Glide.with(itemView)
                .load(url)
                .override(image.width, image.height)
                .into(image)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(image, item.id, url)
            }
        }
    }
}