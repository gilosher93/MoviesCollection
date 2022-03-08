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
import com.example.moviescollection.databinding.ItemMovieBinding
import com.example.moviescollection.di.AppConfig
import com.example.moviescollection.model.Cast
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.ui.diff_utils.CastDiffUtilCallback
import com.example.moviescollection.ui.diff_utils.MovieDiffUtilCallback
import org.koin.java.KoinJavaComponent.inject

class CastAdapter(
    private var items: List<Cast> = listOf()
) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = DataBindingUtil.inflate<ItemCastBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cast,
            parent,
            false
        )
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newItems: List<Cast>) {
        val callback = CastDiffUtilCallback(items, newItems)
        items = newItems

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    class CastViewHolder(
        private val binding: ItemCastBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val appConfig: AppConfig by inject(AppConfig::class.java)

        fun bind(cast: Cast) {
            setImage(cast.profilePath)
            binding.castName.text = cast.name.trim()
        }

        private fun setImage(imagePath: String?) {
            val imagePrefix = appConfig.baseUrl + appConfig.posterSize
            if (imagePrefix.isBlank().not() && imagePath.isNullOrBlank().not()) {
                val imageUrl = imagePrefix + imagePath
                Glide.with(itemView.context)
                    .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(imageUrl)
                    .into(binding.castProfileImage)
            } else {
                binding.castProfileImage.setImageResource(0)
            }
        }
    }
}