package com.example.moviescollection.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.moviescollection.R
import com.example.moviescollection.databinding.ItemCastBinding
import com.example.moviescollection.databinding.ItemVideoPreviewBinding
import com.example.moviescollection.model.VideoPreview
import com.example.moviescollection.network.api.HttpRoutes
import com.example.moviescollection.ui.diff_utils.VideoDiffUtilCallback

class VideoAdapter(
    private var items: List<VideoPreview> = listOf()
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = DataBindingUtil.inflate<ItemVideoPreviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_video_preview,
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newItems: List<VideoPreview>) {
        val callback = VideoDiffUtilCallback(items, newItems)
        items = newItems

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    class VideoViewHolder(
        private val binding: ItemVideoPreviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(videoPreview: VideoPreview) {
            setImage(HttpRoutes.getYoutubeUrl(videoPreview.key))
        }

        private fun setImage(imageUrl: String) {
            Glide.with(itemView.context)
                .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .load(imageUrl)
                .into(binding.videoPreviewImage)
        }
    }
}