package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.reducers.PlayerReducer
import com.branhamplayer.android.sermons.ui.NoSelectionFragment
import com.branhamplayer.android.sermons.ui.PlayerFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.Module
import dagger.Provides

@Module
class PlayerModule(private val activity: SermonsActivity) {

    @Provides
    fun provideDrawableTransitionOptions() = DrawableTransitionOptions.withCrossFade()

    @Provides
    fun provideNoSelectionFragment() = NoSelectionFragment()

    @Provides
    fun providePlayerFragment() = PlayerFragment()

    @Provides
    fun providesRoutingReducer(
        noSelectionFragment: NoSelectionFragment,
        playerFragment: PlayerFragment
    ) = PlayerReducer(activity, noSelectionFragment, playerFragment)

    @Provides
    fun provideRequestManager() = Glide.with(activity)
}
