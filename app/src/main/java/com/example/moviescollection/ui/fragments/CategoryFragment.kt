package com.example.moviescollection.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviescollection.R
import com.example.moviescollection.databinding.FragmentCategoryBinding
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.ui.PaginationScrollListener
import com.example.moviescollection.ui.adapter.MoviesCategoryAdapter
import com.example.moviescollection.view_models.MoviesCategoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val NUM_OF_COLUMNS = 3

class CategoryFragment : Fragment() {

    private lateinit var moviesCategoryAdapter: MoviesCategoryAdapter
    private lateinit var binding: FragmentCategoryBinding
    private val moviesCategoryViewModel: MoviesCategoryViewModel by viewModel()
    private var layoutManager: LinearLayoutManager? = null
    private var paginationListener: PaginationScrollListener? = null

    private val args: CategoryFragmentArgs by navArgs()

    private val categoryType: CategoryType
        get() = args.categoryType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesCategoryViewModel.getMoviesCategory(categoryType)

        layoutManager = GridLayoutManager(requireContext(), NUM_OF_COLUMNS)
        moviesCategoryAdapter = MoviesCategoryAdapter(onMovieClicked = ::onMovieClicked)
        binding.moviesList.adapter = moviesCategoryAdapter
        binding.moviesList.layoutManager = layoutManager

        initPagination()

        binding.toolbar.text = categoryType.title
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        observeMoviesCategory()
    }

    private fun initPagination() {
        paginationListener?.let {
            binding.moviesList.removeOnScrollListener(it)
        }

        layoutManager?.let {
            paginationListener = object : PaginationScrollListener(it) {
                override fun onLoadMore() {
                    moviesCategoryViewModel.loadMoreMovies(categoryType)
                }
            }.also { listener ->
                binding.moviesList.addOnScrollListener(listener)
            }

        }
    }

    private fun observeMoviesCategory() {
        moviesCategoryViewModel.observeMoviesCategory(viewLifecycleOwner) { moviesCategory ->
            val moviesList = moviesCategory.movies.mapNotNull {
                moviesCategoryViewModel.getMovie(it)
            }
            moviesCategoryAdapter.setData(moviesList)
        }
    }

    private fun onMovieClicked(movieDetails: MovieDetails) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToMovieDetailsFragment(movieDetails.id)
        findNavController().navigate(action)
    }

}