package com.amohnacs.amiiborepo.ui.main

import com.amohnacs.amiiborepo.dagger.ActivityScope
import com.amohnacs.amiiborepo.ui.details.DetailsFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
    fun inject(mainFragment: MainFragment)
    fun inject(detailsFragment: DetailsFragment) {
        TODO("Not yet implemented")
    }
}