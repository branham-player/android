package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.ui.NoSelectionFragment
import dagger.Module
import dagger.Provides

@Module
class SermonPlayerModule {

    @Provides
    fun provideNoSelectionFragment() = NoSelectionFragment()
}
