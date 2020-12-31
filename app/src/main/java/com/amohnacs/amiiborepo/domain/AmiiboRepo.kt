package com.amohnacs.amiiborepo.domain

import android.graphics.Bitmap
import android.util.Log
import com.amohnacs.amiiborepo.local.AmiiboDao
import com.amohnacs.amiiborepo.local.ImageStorageHelper
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmiiboRepo @Inject constructor(
    private val amiiboService: AmiiboService,
    private val database: AmiiboDao,
    private val imageStorageHelper: ImageStorageHelper
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
//                    val updateAmiibos = storeAmiiboImagesLocally(it)
                    database.insertAllAmiibo(it).doOnComplete {
                        amiiboResponse.amiibos
                    }.toSingleDefault(it)
                }
            }.doOnError {
                Log.e(AmiiboRepo::class.simpleName, it.message.toString())
            }

    private fun storeAmiiboImagesLocally(amiibos: List<Amiibo>): List<Amiibo> {
        amiibos.forEach { amiibo ->
            amiibo.image?.let {
                val localImagePath = imageStorageHelper.saveToInternalStorage(it, amiibo.tail)
                amiibo.localImagePath = localImagePath
            }
        }
        return amiibos
    }

    fun getSingleAmiibo(tail: String) = database.getAmiibo(tail).subscribeOn(Schedulers.io())

    fun updateAmiiboWithPurchase(amiibo: Amiibo) =
        database.updateAmiibo(amiibo)
            .subscribeOn(Schedulers.io())
            .toSingleDefault(true)

    fun getLocalBitmap(localImagePath: String, tail: String): Bitmap? {
        return localImagePath.let { imageStorageHelper.loadImageFromStorage(it, tail) }
    }

    fun getFilteredAmiibos(isPurchased: Boolean) =
            database.getAmiibosByPurchasedState(isPurchased)
                .subscribeOn(Schedulers.io())
}