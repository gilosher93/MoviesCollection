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
import com.example.moviescollection.databinding.ItemMovieBinding
import com.example.moviescollection.di.AppConfig
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.ui.diff_utils.MovieDiffUtilCallback
import org.koin.java.KoinJavaComponent.inject

class MoviesCategoryAdapter(
    private var items: List<MovieDetails> = listOf(),
    private var onMovieClicked: (movie: MovieDetails) -> Unit
) : RecyclerView.Adapter<MoviesCategoryAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = DataBindingUtil.inflate<ItemMovieBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
        return MovieViewHolder(binding, onMovieClicked)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(newItems: List<MovieDetails>) {
        val callback = MovieDiffUtilCallback(items, newItems)
        items = newItems

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieClicked: (movie: MovieDetails) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val appConfig : AppConfig by inject(AppConfig::class.java)

        fun bind(movie: MovieDetails) {
            setImage(movie.posterPath)
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

        }

    }

}