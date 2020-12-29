package com.amohnacs.amiiborepo.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmiiboRepo @Inject constructor(
    private val amiiboPagingSource: AmiiboPagingSource
) {
    fun fetchAmiibos(): Observable<PagingData<Amiibo>> {
        return Pager(
            config = PagingConfig(
                pageSize = AmiiboPagingSource.PAGE_SIZE_OFFSET_VALUE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                amiiboPagingSource
            }
        ).observable
    }
}