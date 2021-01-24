package com.example.itemsearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itemsearch.R
import com.example.itemsearch.models.Item
import com.squareup.picasso.Picasso

class ImageListAdapter(val item: List<Item>, private val imageClickListener: ImagelClickListener) :
    RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_list_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(item[position])
        holder.itemView.setOnClickListener {
            imageClickListener.onImageClickListener(position)
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return item.size
    }

    //the class is hodling the list view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: Item) {
            val textViewName = itemView.findViewById(R.id.title) as TextView
            val imageView = itemView.findViewById(R.id.imageView) as ImageView
            textViewName.text = user.title
            val picasso = Picasso.get()
            picasso?.apply {
                load(user.media.mediaLink).placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground).into(imageView)
            }
        }
    }
}

interface ImagelClickListener {
    fun onImageClickListener(position: Int)
}