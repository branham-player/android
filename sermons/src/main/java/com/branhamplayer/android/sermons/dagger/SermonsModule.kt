package com.branhamplayer.android.sermons.dagger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class SermonsModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideActivity() = activity

    @Provides
    fun provideContext(): Context = activity.applicationContext
}
