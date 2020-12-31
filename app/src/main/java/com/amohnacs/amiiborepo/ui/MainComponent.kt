package com.amohnacs.amiiborepo.ui

import com.amohnacs.amiiborepo.dagger.ActivityScope
import com.amohnacs.amiiborepo.ui.add.AddFragment
import com.amohnacs.amiiborepo.ui.details.DetailsFragment
import com.amohnacs.amiiborepo.ui.main.MainFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }
    fun inject(mainFragment: MainFragment)
    fun inject(detailsFragment: DetailsFragment)
    fun inject(addFragment: AddFragment)
}