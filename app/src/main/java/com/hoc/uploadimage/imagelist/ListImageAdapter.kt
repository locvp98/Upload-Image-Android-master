package com.hoc.uploadimage.imagelist

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoc.uploadimage.BASE_URL
import com.hoc.uploadimage.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_item_layout.view.*

class ListImageAdapter : PagedListAdapter<String, ListImageAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.image_item_layout, parent, false)
                    .let(ListImageAdapter::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.itemImageView!!
        private val name = itemView.itemTextName!!
        fun bind(item: String?) = item?.let {
            Picasso.get()
                    .load("${BASE_URL}/images/$it")
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_image_black_24dp)
                    .into(image)
            name.text = it
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String?, newItem: String?) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String?, newItem: String?) = oldItem == newItem
        }
    }
}