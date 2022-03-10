package com.example.moviescollection.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.moviescollection.R
import com.example.moviescollection.databinding.ItemMovieBinding
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.repositories.AppRepository
import org.koin.java.KoinJavaComponent.inject

const val JUMBO_TYPE = 1

class RowCategoryAdapter(
    private var items: MutableList<MovieDetails> = mutableListOf(),
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
        val oldSize = items.size

        items.clear()
        items.addAll(newItems)

        notifyItemRangeInserted(oldSize, items.size)
    }

    class MovieAssetViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieClicked: (movie: MovieDetails) -> Unit,
        private val jumbo: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        private val appRepository: AppRepository by inject(AppRepository::class.java)

        fun bind(movie: MovieDetails) {

            setImage(movie.posterPath)
            movie.title?.let {
                binding.movieTitle.text = it.trim()
            }

            binding.root.setOnClickListener {
                onMovieClicked(movie)
            }
        }

        private fun setImage(imagePath: String?) {
            appRepository.getPosterImageUrl(imagePath)?.let { posterImageUrl ->
                Glide.with(itemView.context)
                    .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(posterImageUrl)
                    .into(binding.movieImage)
            } ?: run {
                binding.movieImage.setImageResource(0)
            }

            val widthDimenRes = if (jumbo) R.dimen.jumbo_movie_image_width else R.dimen.movie_image_width
            val layoutParams = binding.movieImage.layoutParams
            layoutParams.width = binding.root.context.resources.getDimensionPixelSize(widthDimenRes)
            binding.movieImage.layoutParams = layoutParams
        }
    }
}