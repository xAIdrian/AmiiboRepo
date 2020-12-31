package com.amohnacs.amiiborepo.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.amiiborepo.domain.AmiiboRepo
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
        val amiiboRepo: AmiiboRepo
) : ViewModel() {

    val amiibos = MutableLiveData<List<Amiibo>>().apply { value = emptyList() }
    val errorEvent = MutableLiveData<String>()
    val emptyStateEvent = MutableLiveData<Boolean>()
    val loadingEvent = MutableLiveData<Boolean>()
    val purchasedEvent = MutableLiveData<Boolean>()

    private var amiibosFiltered = false

    @SuppressLint("CheckResult")
    fun loadAmiibos() {
        loadingEvent.value = true
        emptyStateEvent.value = false

        amiiboRepo.fetchFreshAmiibos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { amiibosList ->
                            loadingEvent.value = false
                            if (amiibosList?.isNotEmpty() == true) {
                                amiibos.value = amiibosList
                            } else {
                                emptyStateEvent.value = true
                            }
                        }, {
                    loadingEvent.value = false
                    errorEvent.value = it.message.toString()
                })
    }

    fun getStoredImageBitmap(localImagePath: String) = amiiboRepo.getLocalBitmap(localImagePath)

    @SuppressLint("CheckResult")
    fun filterAmiibos() {
        loadingEvent.value = true
        emptyStateEvent.value = false

        amiibosFiltered = !amiibosFiltered
        purchasedEvent.value = amiibosFiltered
        if (amiibosFiltered) {
            amiiboRepo.getFilteredAmiibos(amiibosFiltered)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { amiibosList ->
                                loadingEvent.value = false
                                if (amiibosList?.isNotEmpty() == true) {
                                    amiibos.value = amiibosList
                                } else {
                                    amiibos.value = emptyList()
                                    emptyStateEvent.value = true
                                }
                            },
                            {
                                loadingEvent.value = false
                                errorEvent.value = it.message.toString()
                            })
        } else {
            loadAmiibos()
        }
    }
}