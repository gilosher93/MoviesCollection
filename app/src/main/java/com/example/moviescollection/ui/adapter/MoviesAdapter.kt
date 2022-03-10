package com.example.moviescollection.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviescollection.R
import com.example.moviescollection.databinding.ItemMoviesListBinding
import com.example.moviescollection.repositories.MoviesRepository
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.ui.PaginationScrollListener
import com.example.moviescollection.ui.diff_utils.MovieCategoryDiffUtilCallback
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MoviesAdapter(
    private var items: List<MovieCategory> = listOf(),
    private var lifecycleScope: LifecycleCoroutineScope,
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
        return RowViewHolder(binding, lifecycleScope, onMovieClicked, onCategoryClicked)
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
        private var lifecycleScope: LifecycleCoroutineScope,
        private val onMovieClicked: (movie: MovieDetails) -> Unit,
        private var onCategoryClicked: (type: CategoryType) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val moviesRepository: MoviesRepository by inject(MoviesRepository::class.java)
        private var layoutManager: LinearLayoutManager? = null
        private var paginationListener: PaginationScrollListener? = null

        private lateinit var rowAdapter: RowCategoryAdapter
        private lateinit var movieCategory: MovieCategory

        fun bind(movieCategory: MovieCategory) {
            this.movieCategory = movieCategory
            val moviesList = movieCategory.movies.mapNotNull { moviesRepository.getMovie(it) }.toMutableList()

            rowAdapter = RowCategoryAdapter(moviesList, onMovieClicked, movieCategory.bigImages)
            layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.rowTitle.text = movieCategory.categoryType.title
            binding.moviesRowList.adapter = rowAdapter
            binding.moviesRowList.layoutManager = layoutManager
            initPagination()

            binding.moreButton.setOnClickListener {
                onCategoryClicked(movieCategory.categoryType)
            }
        }

        private fun initPagination() {
            paginationListener?.let {
                binding.moviesRowList.removeOnScrollListener(it)
            }

            layoutManager?.let { lm ->
                paginationListener = object : PaginationScrollListener(lm) {
                    override fun onLoadMore() {
                        lifecycleScope.launch {
                            moviesRepository.loadMoreMovies(movieCategory.categoryType)
                            val moviesList = movieCategory.movies.mapNotNull {
                                moviesRepository.getMovie(it)
                            }
                            rowAdapter.setData(moviesList)
                        }
                    }
                }.also { listener ->
                    binding.moviesRowList.addOnScrollListener(listener)
                }

            }
        }

    }

}