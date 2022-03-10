package com.example.moviescollection.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.moviescollection.databinding.FragmentMovieDetailsBinding
import com.example.moviescollection.model.MovieDetails
import com.example.moviescollection.network.results.ApiResult
import com.example.moviescollection.ui.adapter.CastAdapter
import com.example.moviescollection.ui.adapter.VideoAdapter
import com.example.moviescollection.view_models.MovieDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment : Fragment() {

    private lateinit var castAdapter: CastAdapter
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var binding: FragmentMovieDetailsBinding

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val movieId: Int
        get() = args.movieId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMovie()

        castAdapter = CastAdapter()
        binding.castsList.adapter = castAdapter
        binding.castsList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        videoAdapter = VideoAdapter()
        binding.videosList.adapter = videoAdapter
        binding.videosList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        binding.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        movieDetailsViewModel.getMovieById(movieId)
    }

    private fun observeMovie() {
        movieDetailsViewModel.observeMovie(viewLifecycleOwner) { movieDetailsResult ->
            when(movieDetailsResult) {
                is ApiResult.Loading -> {
                    setLoading(true)
                }
                is ApiResult.Success -> {
                    setContent(movieDetailsResult.data)
                }
                is ApiResult.Error -> {
                    setLoading(false)
                }
            }

        }
    }

    private fun setLoading(show: Boolean) {
        if (show) {
            binding.movieDetailsProgressBar.visibility = VISIBLE
        } else {
            binding.movieDetailsProgressBar.visibility = GONE
        }
    }

    private fun setContent(movie: MovieDetails) {

        setBackdrop(movie)
        movie.title?.let { binding.collapsingToolbar.title = it }

        if (movie.hasFullData()) {
            binding.movieContent.visibility = VISIBLE
            setPoster(movie)
            binding.budgetLabel.text = "Budget: ${movie.budget}"
            binding.releaseDateLabel.text = "Release: ${movie.releaseDate}"
            binding.runtimeLabel.text = "Runtime: ${movie.runtime} min"
            binding.ratingLabel.text = "Rating: ${movie.voteAverage}/10"
            binding.overviewLabel.text = movie.overview.trim()
            movie.status?.let { binding.statusLabel.text = "Status: $it" }
            movie.castList?.let { if (it.isNotEmpty()) castAdapter.setData(it) }
            movie.videoPreviewList?.let { if (it.isNotEmpty()) videoAdapter.setData(it) }
            setLoading(false)
        }
    }

    private fun setBackdrop(movie: MovieDetails) {
        movieDetailsViewModel.getBackdropImageUrl(movie.backdropPath)?.let { backdropImageUrl ->
            context?.let {
                Glide.with(it)
                    .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(backdropImageUrl)
                    .into(binding.movieBackdrop)
            }
        }
    }

    private fun setPoster(movie: MovieDetails) {
        movieDetailsViewModel.getPosterImageUrl(movie.posterPath)?.let { posterImageUrl ->
            context?.let {
                Glide.with(it)
                    .applyDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(posterImageUrl)
                    .into(binding.moviePoster)
            }
        }
    }
}