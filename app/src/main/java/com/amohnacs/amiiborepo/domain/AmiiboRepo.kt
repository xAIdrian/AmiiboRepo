package com.amohnacs.amiiborepo.domain

import android.util.Log
import androidx.annotation.WorkerThread
import com.amohnacs.amiiborepo.local.AmiiboDao
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmiiboRepo @Inject constructor(
    private val amiiboService: AmiiboService,
    private val database: AmiiboDao
) {
    fun fetchFreshAmiibos(): Single<List<Amiibo>> {
        return database.getAmiibos()
            .subscribeOn(Schedulers.io())
            .flatMap { amiiboList ->
                if (amiiboList.isEmpty()) {
                    updateAmiiboLocalStorageInBulk()
                } else {
                    Single.fromObservable(Observable.fromArray(amiiboList))
                }
            }
    }

    private fun updateAmiiboLocalStorageInBulk(): Single<List<Amiibo>> =
        amiiboService.getAmiibosResponse()
            .subscribeOn(Schedulers.io())
            .flatMap { amiiboResponse ->
                amiiboResponse.amiibos?.let {
                    database.insertAllAmiibo(it).doOnComplete {
                        amiiboResponse.amiibos
                    }.toSingleDefault(it)
                }
            }
}