package com.example.moviescollection.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.moviescollection.R
import com.example.moviescollection.databinding.ItemMovieBinding
import com.example.moviescollection.di.AppConfig
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.ui.diff_utils.MovieDiffUtilCallback
import org.koin.java.KoinJavaComponent.inject

const val JUMBO_TYPE = 1

class RowCategoryAdapter(
    private var items: List<MovieDetails> = listOf(),
    private var onMovieClicked: (movie: MovieDetails) -> Unit,
    private var jumbo: Boolean = false
) : RecyclerView.Adapter<RowCategoryAdapter.MovieAssetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAssetViewHolder {
        val binding = DataBindingUtil.inflate<ItemMovieBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
        return MovieAssetViewHolder(binding, onMovieClicked, jumbo)
    }

    override fun onBindViewHolder(holder: MovieAssetViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newItems: List<MovieDetails>) {
        val callback = MovieDiffUtilCallback(items, newItems)
        items = newItems

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    class MovieAssetViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieClicked: (movie: MovieDetails) -> Unit,
        private val jumbo: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        private val appConfig : AppConfig by inject(AppConfig::class.java)

        fun bind(movie: MovieDetails) {

            setImage(movie.posterPath)
            binding.movieTitle.text = movie.title.trim()

            binding.root.setOnClickListener {
                onMovieClicked(movie)
            }
        }

        private fun setImage(imagePath: String) {
            val imagePrefix = appConfig.baseUrl + appConfig.posterSize
            if (imagePrefix.isNotEmpty() && imagePath.isNotEmpty()) {
                val imageUrl = imagePrefix + imagePath
                Glide.with(itemView.context)
                    .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(imageUrl)
                    .into(binding.movieImage)
            } else {
                binding.movieImage.setImageResource(0)
            }

            val widthDimenRes = if (jumbo) R.dimen.jumbo_movie_image_width else R.dimen.movie_image_width
            val layoutParams = binding.movieImage.layoutParams
            layoutParams.width = binding.root.context.resources.getDimensionPixelSize(widthDimenRes)
            binding.movieImage.layoutParams = layoutParams
        }
    }
}