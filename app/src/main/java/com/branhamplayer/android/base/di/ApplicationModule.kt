package com.branhamplayer.android.base.di

import android.content.Context
import com.branhamplayer.android.App
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: App) {

    @Provides
    fun getContext(): Context = application
}
