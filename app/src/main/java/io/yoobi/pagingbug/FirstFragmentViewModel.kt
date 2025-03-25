package io.yoobi.pagingbug

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

class FirstFragmentViewModel: ViewModel() {
    val numbers: Flow<PagingData<Int>> = Pager(
        config = PagingConfig(pageSize = 50, enablePlaceholders = false),
        pagingSourceFactory = { RandomNumberPagingSource() }
    ).flow
}
