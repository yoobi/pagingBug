package io.yoobi.pagingbug

import androidx.paging.PagingSource
import androidx.paging.PagingState

class RandomNumberPagingSource : PagingSource<Int, Int>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Int> {
        val page = params.key ?: 0  // Start from page 0
        val pageSize = 50           // Each page contains 50 numbers

        val start = page * pageSize  // Calculate start number
        val end = start + pageSize   // Calculate end number
        val data = (start until end).toList()  // Generate sequential numbers

        return LoadResult.Page(
            data = data,
//            prevKey = null,
//            nextKey = null
            prevKey = if (page == 0) null else page - 1,  // No previous page if first page
            nextKey = page + 1  // Always increment the page
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Int>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { anchorPage ->
                val pageIndex = state.pages.indexOf(anchorPage)
                if (pageIndex == 0) null else state.pages[pageIndex - 1].nextKey
            }
        }
    }
}
