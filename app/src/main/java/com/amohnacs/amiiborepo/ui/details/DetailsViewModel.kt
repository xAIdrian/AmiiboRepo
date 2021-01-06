package com.amohnacs.amiiborepo.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.amiiborepo.domain.AmiiboRepo
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
        private val amiiboRepo: AmiiboRepo
) : ViewModel() {

    val amiibo = MutableLiveData<Amiibo>()
    val amiiboPurchasedState = MutableLiveData<Boolean>().apply { amiibo.value?.isPurchased }
    val errorEvent = MutableLiveData<String>()

    private val disposer = CompositeDisposable()

    fun getAmiiboDetails(tail: String) {
        disposer.add(amiiboRepo.getSingleAmiibo(tail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { amiiboy ->
                            amiibo.value = amiiboy
                        }, {
                    errorEvent.value = it.message.toString()
                }
                ))
    }

    fun purchaseAmiibo() {
        if (amiibo.value != null) {
            val workingAmiiboy = amiibo.value
            workingAmiiboy?.isPurchased = true
            amiibo.value = workingAmiiboy

            disposer.add(amiiboRepo.updateAmiiboWithPurchase(amiibo.value!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { wasPurchased ->
                                amiiboPurchasedState.value = wasPurchased
                            }, {
                        errorEvent.value = it.message.toString()
                    }
                    ))
        } else {
            errorEvent.value = "Something went wrong with purchase"
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposer.clear()
    }
}
