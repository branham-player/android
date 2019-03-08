package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.ui.PlayerFragment
import dagger.Module
import dagger.Provides

@Module
class PlayerModule {

    @Provides
    fun providePlayerFragment() = PlayerFragment()
}
