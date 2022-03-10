package com.example.moviescollection.ui.diff_utils

import androidx.recyclerview.widget.DiffUtil
import com.example.moviescollection.model.Cast
import com.example.moviescollection.model.MovieDetails

class CastDiffUtilCallback(
    private var newList: List<Cast>,
    private var oldList: List<Cast>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].profilePath == newList[newItemPosition].profilePath
    }
}