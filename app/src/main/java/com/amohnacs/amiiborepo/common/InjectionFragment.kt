package com.amohnacs.amiiborepo.common

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amohnacs.amiiborepo.AmiiboApp
import com.amohnacs.amiiborepo.ui.main.MainActivity
import com.amohnacs.amiiborepo.ui.main.MainComponent

open class InjectionFragment : Fragment() {

    lateinit var mainDaggerComponent: MainComponent

    override fun onAttach(context: Context) {
        val mainActivity = activity as MainActivity
        val application = mainActivity.applicationContext as AmiiboApp
        mainDaggerComponent = application.appComponent.mainComponent().create()
        super.onAttach(context)
    }
}