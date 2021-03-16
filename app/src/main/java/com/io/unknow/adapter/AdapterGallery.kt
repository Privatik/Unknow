package com.io.unknow.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.unknow.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class AdapterGallery(private val context: Context, private val list: List<String>, private val imagesSelected: MutableSharedFlow<String>): RecyclerView.Adapter<AdapterGallery.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder = ImageViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false), imagesSelected
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageUrl = list[position]

        Glide.with(context)
            .load(holder.imageUrl)
            .centerCrop()
            .into(holder.imageView)
    }

    class ImageViewHolder(item: View, val imagesSelected: MutableSharedFlow<String>): RecyclerView.ViewHolder(item), View.OnClickListener{
        val imageView = itemView.findViewById<ImageView>(R.id.photo)
        private val selectedPhoto = itemView.findViewById<TextView>(R.id.selected_photo)
        lateinit var imageUrl: String

        init {
            item.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.i("Gallery","click ${selectedPhoto.visibility == View.VISIBLE}")

            if (selectedPhoto.visibility == View.VISIBLE) {
                selectedPhoto.visibility = View.GONE
            } else {
                selectedPhoto.visibility = View.VISIBLE
            }

            CoroutineScope(Dispatchers.Default).launch{
                Log.i("GALLERY","load $imageUrl")
                imagesSelected.emit(imageUrl)
            }
        }
    }
}