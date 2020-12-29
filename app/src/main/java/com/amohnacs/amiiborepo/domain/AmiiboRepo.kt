package com.amohnacs.amiiborepo.domain

import com.amohnacs.amiiborepo.model.AmiiboResponse
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmiiboRepo @Inject constructor(
    private val amiiboService: AmiiboService
) {
    fun fetchAmiibosInBulk() = amiiboService.getAmiibosResponse().subscribeOn(Schedulers.io())
}