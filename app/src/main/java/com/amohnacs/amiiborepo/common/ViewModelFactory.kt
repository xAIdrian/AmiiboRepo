package com.amohnacs.amiiborepo.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory<T> @Inject constructor(
    private val viewModelProvider: Provider<T>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModelProvider.get() as T
}