package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.reducers.DrawerReducer
import dagger.Module
import dagger.Provides

@Module
class DrawerModule {

    @Provides
    fun provideDrawerReducer() = DrawerReducer()
}
