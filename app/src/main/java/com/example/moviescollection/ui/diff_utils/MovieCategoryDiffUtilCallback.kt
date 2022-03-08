package com.example.moviescollection.ui.diff_utils

import androidx.recyclerview.widget.DiffUtil
import com.example.moviescollection.model.MovieCategory
import com.example.moviescollection.model.MovieDetails

class MovieCategoryDiffUtilCallback(
    private var newList: List<MovieCategory>,
    private var oldList: List<MovieCategory>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].categoryType == newList[newItemPosition].categoryType
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].movies.size == newList[newItemPosition].movies.size
    }
}