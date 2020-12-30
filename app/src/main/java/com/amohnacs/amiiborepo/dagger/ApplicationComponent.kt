package com.amohnacs.amiiborepo.dagger

import com.amohnacs.amiiborepo.ui.MainComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, SubcomponentsModule::class])
interface ApplicationComponent {
    fun mainComponent(): MainComponent.Factory
}