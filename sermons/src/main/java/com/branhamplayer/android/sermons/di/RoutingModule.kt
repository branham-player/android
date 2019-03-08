package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.reducers.RoutingReducer
import com.branhamplayer.android.sermons.ui.NoSelectionFragment
import com.branhamplayer.android.sermons.ui.PlayerFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import dagger.Module
import dagger.Provides

@Module
class RoutingModule {

    @Provides
    fun providesRoutingReducer(
        activity: SermonsActivity,
        noSelectionFragment: NoSelectionFragment,
        playerFragment: PlayerFragment
    ) = RoutingReducer(activity, noSelectionFragment, playerFragment)
}
