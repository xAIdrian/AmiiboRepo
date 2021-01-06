package com.amohnacs.amiiborepo.ui.add

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amohnacs.amiiborepo.domain.AmiiboRepo
import com.amohnacs.amiiborepo.local.ImageStorageHelper
import com.amohnacs.amiiborepo.model.Amiibo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class AddViewModel @Inject constructor(
    private val amiiboRepo: AmiiboRepo,
    private val imageStorageHelper: ImageStorageHelper
) : ViewModel() {

    val successfulSaveEvent = MutableLiveData<Boolean>()
    val errorEvent = MutableLiveData<String>()

    private val disposer = CompositeDisposable()

    private var workingAmiibo = Amiibo(
        tail = generateTail()
    )

    override fun onCleared() {
        super.onCleared()
        disposer.clear()
    }

    fun updateAmiiboImage(bitmap: Bitmap) {
        val imagePath = imageStorageHelper.saveToInternalStorage(bitmap, workingAmiibo.tail)
        workingAmiibo.localImagePath = imagePath
    }

    /**
     * Yes, we can update our fields one at a time as we receive idle text input form EditText to preserve state
     * Doing it in a single (or double if you include the image) go to save time
     */
    fun saveAmiibo(
        name: String,
        series: String,
        type: String,
        isPurchased: Boolean
    ) {
        val newWorkingAmiibo = workingAmiibo.copy(
            name = name,
            amiiboSeries = series,
            type = type,
            isPurchased = isPurchased,
            userCreated = true
        )
        disposer.add(amiiboRepo.addSingleAmiibo(newWorkingAmiibo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    successfulSaveEvent.value = it > 0
                }, {
                    errorEvent.value = it.message.toString()
                }
            ))
    }

    private fun generateTail() = UUID.randomUUID().toString().substring(0, 5)
}