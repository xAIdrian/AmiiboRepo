package com.amohnacs.amiiborepo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.amohnacs.amiiborepo.dagger.ApplicationModule
import com.amohnacs.amiiborepo.dagger.DaggerApplicationComponent

class AmiiboApp: Application() {
    lateinit var appComponent: DaggerApplicationComponent

    override fun onCreate() {
        super.onCreate()
        this.appComponent = this.initDagger() as DaggerApplicationComponent
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private fun initDagger() = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
}