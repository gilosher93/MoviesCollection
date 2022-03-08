package com.example.moviescollection.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviescollection.R
import com.example.moviescollection.databinding.ItemMoviesListBinding
import com.example.moviescollection.di.MoviesManager
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.ui.diff_utils.MovieCategoryDiffUtilCallback
import com.example.moviescollection.ui.diff_utils.MovieDiffUtilCallback
import org.koin.java.KoinJavaComponent.inject

class MoviesAdapter(
    private var items: List<MovieCategory> = listOf(),
    private var onMovieClicked: (movie: MovieDetails) -> Unit,
    private var onCategoryClicked: (type: CategoryType) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemMoviesListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_movies_list,
            parent,
            false
        )
        return RowViewHolder(binding, onMovieClicked, onCategoryClicked)
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
        val callback = MovieCategoryDiffUtilCallback(items, newItems)
        items = newItems

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    class RowViewHolder(
        private val binding: ItemMoviesListBinding,
        private val onMovieClicked: (movie: MovieDetails) -> Unit,
        private var onCategoryClicked: (type: CategoryType) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val moviesManager: MoviesManager by inject(MoviesManager::class.java)

        fun bind(movieCategory: MovieCategory) {

            val moviesList = movieCategory.movies.mapNotNull { moviesManager.getMovie(it) }

            val adapter = RowCategoryAdapter(moviesList, onMovieClicked, movieCategory.bigImages)
            binding.rowTitle.text = movieCategory.categoryType.title
            binding.moviesRowList.adapter = adapter
            binding.moviesRowList.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.moreButton.setOnClickListener {
                onCategoryClicked(movieCategory.categoryType)
            }
        }
    }

}