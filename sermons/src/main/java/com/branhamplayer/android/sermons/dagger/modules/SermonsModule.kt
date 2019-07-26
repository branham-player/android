package com.branhamplayer.android.sermons.dagger.modules

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.sermons.ui.SermonsActivity
import dagger.Module
import dagger.Provides

@Module
class SermonsModule(private val activity: SermonsActivity) {

    @Provides
    fun provideActivity(): AppCompatActivity = activity

    @Provides
    fun provideContext(): Context = activity.applicationContext

    @Provides
    fun provideSermonsActivity() = activity
}
