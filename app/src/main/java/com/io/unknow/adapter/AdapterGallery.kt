package com.io.unknow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.unknow.R

class AdapterGallery(val context: Context, val list: List<String>, val imagesSelected: MutableList<String>): RecyclerView.Adapter<AdapterGallery.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder = ImageViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageUrl = list[position]

        Glide.with(context)
            .load(holder.imageUrl)
            .centerCrop()
            .into(holder.imageView)
    }

    inner class ImageViewHolder(item: View): RecyclerView.ViewHolder(item), View.OnClickListener{
        val imageView = itemView.findViewById<ImageView>(R.id.photo)
        lateinit var imageUrl: String
        private var isSelected = false


        override fun onClick(v: View?) {
            if (isSelected) {
                imageView.layoutParams.height -= 10
                imageView.layoutParams.width -= 10

                imagesSelected.remove(imageUrl)
            } else {
                imageView.layoutParams.height += 10
                imageView.layoutParams.width += 10

                imagesSelected.add(imageUrl)
            }
            isSelected = !isSelected
        }
    }
}