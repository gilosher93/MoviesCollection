package com.example.moviescollection.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviescollection.R
import com.example.moviescollection.databinding.ItemMoviesListBinding
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails

class MoviesAdapter(
    private var items: List<MovieCategory> = listOf(),
    private var onMovieClicked: (movie: MovieDetails) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemMoviesListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_movies_list,
            parent,
            false
        )
        return RowViewHolder(binding, onMovieClicked)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RowViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return JUMBO_TYPE
        }
        return super.getItemViewType(position)
    }

    fun setData(newItems: List<MovieCategory>) {
        items = newItems
        notifyDataSetChanged()
    }

    class RowViewHolder(
        private val binding: ItemMoviesListBinding,
        private val onMovieClicked: (movie: MovieDetails) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCategory: MovieCategory) {

            val adapter = RowCategoryAdapter(movieCategory.movies, onMovieClicked, movieCategory.bigImages)
            binding.rowTitle.text = movieCategory.title
            binding.moviesRowList.adapter = adapter
            binding.moviesRowList.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        }
    }

}