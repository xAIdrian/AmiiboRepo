package com.amohnacs.amiiborepo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amohnacs.amiiborepo.domain.AmiiboRepo
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
        val amiiboRepo: AmiiboRepo
) : ViewModel() {

    val amiibos = MutableLiveData<List<Amiibo>>().apply { value = emptyList() }
    val errorEvent = MutableLiveData<String>()
    val emptyStateEvent = MutableLiveData<Any>()

    fun loadAmiibos(): Observable<PagingData<Amiibo>> = amiiboRepo.fetchAmiibos()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { e -> errorEvent.value = e.message }
}