package com.example.moviescollection.ui.diff_utils

import androidx.recyclerview.widget.DiffUtil
import com.example.moviescollection.model.MovieDetails

class MovieDiffUtilCallback(
    private var newList: List<MovieDetails>,
    private var oldList: List<MovieDetails>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].voteAverage == newList[newItemPosition].voteAverage
    }
}