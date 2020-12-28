package com.amohnacs.amiiborepo.ui.main

import com.amohnacs.amiiborepo.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
    fun inject(mainFragment: MainFragment)
}