package io.yoobi.pagingbug

import androidx.paging.PagingSource
import androidx.paging.PagingState

class RandomNumberPagingSource : PagingSource<Int, Int>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Int> {
//        val page = params.key ?: 0
        val data = List(50) { it }.shuffled()

        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null
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
