package com.example.moviescollection.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {

    private val visibleThreshold = 8
    private var currentPage = 1
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 1

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = view.childCount
        val totalItemCount = layoutManager.itemCount

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && totalItemCount - visibleItemCount < firstVisibleItem + visibleThreshold) {

            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()

}