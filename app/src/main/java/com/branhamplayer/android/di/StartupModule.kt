package com.branhamplayer.android.di

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class StartupModule(private val activity: Activity) {

    @Provides
    fun provideActivity() = activity

    @Provides
    fun provideContext(): Context = activity.applicationContext
}
