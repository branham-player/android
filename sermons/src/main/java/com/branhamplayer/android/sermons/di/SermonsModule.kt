package com.branhamplayer.android.sermons.di

import android.content.Context
import com.branhamplayer.android.sermons.adapters.SermonsAdapter
import com.branhamplayer.android.sermons.ui.SermonListFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import com.branhamplayer.android.ui.DrawerHeaderViewBinder
import dagger.Module
import dagger.Provides

@Module
class SermonsModule(private val activity: SermonsActivity) {

    @Provides
    fun provideActivity() = activity

    @Provides
    fun provideContext(): Context = activity

    @Provides
    fun provideDrawerHeaderViewBinder() = DrawerHeaderViewBinder()
}