package com.amohnacs.amiiborepo

import android.app.Application
import com.amohnacs.amiiborepo.dagger.DaggerApplicationComponent

class AmiiboApp: Application() {
    lateinit var appComponent: DaggerApplicationComponent

    override fun onCreate() {
        super.onCreate()
        this.appComponent = this.initDagger() as DaggerApplicationComponent
    }

    private fun initDagger() = DaggerApplicationComponent.builder().build()
}