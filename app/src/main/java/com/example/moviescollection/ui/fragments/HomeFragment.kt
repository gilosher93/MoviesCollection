package com.example.moviescollection.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviescollection.R
import com.example.moviescollection.databinding.FragmentHomeBinding
import com.example.moviescollection.model.CategoryType
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.ui.adapter.MoviesAdapter
import com.example.moviescollection.view_models.HomeViewModel
import com.example.moviescollection.view_models.MovieResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getMoviesCatalog()

        moviesAdapter = MoviesAdapter(
            onMovieClicked = ::onMovieClicked,
            onCategoryClicked = ::onCategoryClicked
        )
        binding.moviesList.adapter = moviesAdapter
        binding.moviesList.layoutManager = LinearLayoutManager(requireContext())

        observeAllMovies()
    }

    private fun observeAllMovies() {
        homeViewModel.observeAllMovies(viewLifecycleOwner) { moviesResult ->
            when (moviesResult) {
                is MovieResult.Loading -> {
                    setLoading(true)
                }
                is MovieResult.Success -> {
                    setLoading(false)
                    moviesAdapter.setData(moviesResult.allMovies)
                }
                is MovieResult.Error -> {
                    setLoading(false)
                }
            }
        }
    }

    private fun setLoading(show: Boolean) {
        if (show) {
            binding.homeProgressBar.visibility = View.VISIBLE
        } else {
            binding.homeProgressBar.visibility = View.GONE
        }
    }

    private fun onMovieClicked(movieDetails: MovieDetails) {
        val action = HomeFragmentDirections.actionFirstFragmentToSecondFragment(movieDetails.id)
        findNavController().navigate(action)
    }

    private fun onCategoryClicked(categoryType: CategoryType) {
        val action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment(categoryType)
        findNavController().navigate(action)
    }

}