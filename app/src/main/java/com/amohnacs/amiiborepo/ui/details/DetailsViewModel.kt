package com.amohnacs.amiiborepo.ui.details

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.amiiborepo.domain.AmiiboRepo
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val amiiboRepo: AmiiboRepo
) : ViewModel(){

    val amiibo = MutableLiveData<Amiibo>()
    val amiiboPurchasedState = MutableLiveData<Boolean>().apply { amiibo.value?.isPurchased }
    val errorEvent = MutableLiveData<String>()

    fun getAmiiboDetails(tail: String) =
        amiiboRepo.getSingleAmiibo(tail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { amiiboy ->
                    amiibo.value = amiiboy
                }, {
                    errorEvent.value = it.message.toString()
                }
            )

    @SuppressLint("CheckResult")
    fun purchaseAmiibo() {
        if (amiibo.value != null) {
            val workingAmiiboy = amiibo.value
            workingAmiiboy?.isPurchased = true
            amiibo.value = workingAmiiboy

            amiiboRepo.updateAmiiboWithPurchase(amiibo.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { wasPurchased ->
                        amiiboPurchasedState.value = wasPurchased
                    }, {
                     errorEvent.value = it.message.toString()
                    }
                )
        } else {
            errorEvent.value = "Something went wrong with purchase"
        }
    }

    fun getStoredImageBitmap(localImagePath: String) = amiiboRepo.getLocalBitmap(localImagePath)
}
