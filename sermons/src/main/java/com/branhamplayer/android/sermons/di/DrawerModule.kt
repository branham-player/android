package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.reducers.DrawerReducer
import com.branhamplayer.android.ui.DrawerHeaderViewBinder
import dagger.Module
import dagger.Provides

@Module
class DrawerModule {

    @Provides
    fun provideDrawerHeaderViewBinder() = DrawerHeaderViewBinder()

    @Provides
    fun provideDrawerReducer() = DrawerReducer()
}
