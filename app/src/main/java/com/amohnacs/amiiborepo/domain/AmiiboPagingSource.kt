package com.amohnacs.amiiborepo.domain

import androidx.paging.rxjava2.RxPagingSource
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmiiboPagingSource @Inject constructor(
    private val amiiboService: AmiiboService
) : RxPagingSource<Int, Amiibo>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Amiibo>> {
        val pagingOffsetKeyFromParams = params.key ?: STARTING_OFFSET_VALUE
        return amiiboService.getAmiibosResponse()
            .subscribeOn(Schedulers.io())
            .flatMap { Observable.fromIterable(it.amiibos) }
            .toList()
            .map { amiibos -> toLoadResult(amiibos, pagingOffsetKeyFromParams) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        amiibo: MutableList<Amiibo>,
        pagingOffsetKey: Int
    ): LoadResult<Int, Amiibo> {
        return LoadResult.Page(
            data = amiibo,
            prevKey = if (pagingOffsetKey == STARTING_OFFSET_VALUE) null else pagingOffsetKey - PAGE_SIZE_OFFSET_VALUE,
            nextKey = if (amiibo.isEmpty()) null else pagingOffsetKey + PAGE_SIZE_OFFSET_VALUE
        )
    }

    companion object {
        private const val STARTING_OFFSET_VALUE = 0
        internal const val PAGE_SIZE_OFFSET_VALUE = 20
    }
}