package com.amohnacs.amiiborepo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.amiiborepo.domain.AmiiboRepo
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
    private val disposer = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposer.clear()
    }

    fun loadAmiibos(calledWithFilterButton: Boolean = false) {
        loadingEvent.value = true
        emptyStateEvent.value = false

        if (calledWithFilterButton) {
            amiibosFiltered = !amiibosFiltered
        }
        purchasedEvent.value = amiibosFiltered
        disposer.add(
                if (amiibosFiltered) {
                    amiiboRepo.getFilteredAmiibos(amiibosFiltered)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        onAmiiboResult(it)
                                    },
                                    {
                                        loadingEvent.value = false
                                        errorEvent.value = it.message.toString()
                                    })
                } else {
                    amiiboRepo.fetchAmiibos()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        onAmiiboResult(it)
                                    }, {
                                loadingEvent.value = false
                                errorEvent.value = it.message.toString()
                            })
                })
    }

    private fun onAmiiboResult(amiibosList: List<Amiibo>?) {
        loadingEvent.value = false
        if (amiibosList?.isNotEmpty() == true) {
            amiibos.value = amiibosList
        } else {
            amiibos.value = emptyList()
            emptyStateEvent.value = true
        }
    }
}