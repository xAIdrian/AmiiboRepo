package com.amohnacs.amiiborepo.domain

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
                    // our initial load
                    updateAmiiboLocalStorageInBulk()
                } else {
                    val usersAmiibosAtTopList = ArrayList<Amiibo>().apply {
                        addAll(getLocalImages(amiiboList.filter { it.userCreated == true }) as ArrayList<Amiibo>)
                        addAll(amiiboList.filter { it.userCreated == false })
                    }
                    Single.fromObservable(Observable.fromArray(usersAmiibosAtTopList))
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
            }.doOnError {
                Log.e(AmiiboRepo::class.simpleName, it.message.toString())
            }

    fun getSingleAmiibo(tail: String) =
        database.getAmiibo(tail)
            .subscribeOn(Schedulers.io())
            .map {
                it.apply{
                    localImage = it.localImagePath?.let { localImagePath ->
                        imageStorageHelper.loadImageFromStorage(localImagePath)
                    }
                }
            }

    fun updateAmiiboWithPurchase(amiibo: Amiibo) =
        database.updateAmiibo(amiibo)
            .subscribeOn(Schedulers.io())
            .toSingleDefault(true)

    fun getFilteredAmiibos(isPurchased: Boolean) =
            database.getAmiibosByPurchasedState(isPurchased)
                .subscribeOn(Schedulers.io())
                .map { usersAmiibos ->
                    getLocalImages(usersAmiibos)
                }

    private fun getLocalImages(usersAmiibos: List<Amiibo>): List<Amiibo> =
        usersAmiibos.apply {
            forEach {
                if (!it.localImagePath.isNullOrEmpty()) {
                    it.localImage = it.localImagePath?.let { localImagePath ->
                        imageStorageHelper.loadImageFromStorage(localImagePath)
                    }
                }
            }
        }

    fun addSingleAmiibo(workingAmiibo: Amiibo) =
        database.insertAmiibo(workingAmiibo)
            .subscribeOn(Schedulers.io())
}