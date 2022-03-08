package com.example.moviescollection.ui.diff_utils

import androidx.recyclerview.widget.DiffUtil
import com.example.moviescollection.model.VideoPreview

class VideoDiffUtilCallback(
    private var newList: List<VideoPreview>,
    private var oldList: List<VideoPreview>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[newItemPosition].key
    }
}