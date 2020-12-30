package com.amohnacs.amiiborepo.dagger

import com.amohnacs.amiiborepo.ui.MainComponent
import dagger.Module

@Module(subcomponents = [MainComponent::class])
class SubcomponentsModule {}