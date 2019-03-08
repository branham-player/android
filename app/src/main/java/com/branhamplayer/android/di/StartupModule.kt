package com.branhamplayer.android.di

import android.content.Context
import com.branhamplayer.android.ui.StartupActivity
import dagger.Module
import dagger.Provides

@Module
class StartupModule(private val startupActivity: StartupActivity) {

    @Provides
    fun provideActivity() = startupActivity

    @Provides
    fun provideContext(): Context = startupActivity.applicationContext
}
