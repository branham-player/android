package com.branhamplayer.android.sermons.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.sermons.adapters.SermonsAdapter
import com.branhamplayer.android.sermons.ui.SermonListFragment
import com.branhamplayer.android.ui.DrawerHeaderViewBinder
import dagger.Module
import dagger.Provides

@Module
class SermonsModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideActivity() = activity

    @Provides
    fun provideContext(): Context = activity.applicationContext

    @Provides
    fun provideDrawerHeaderViewBinder() = DrawerHeaderViewBinder()

    @Provides
    fun provideSermonsAdapter(context: Context) = SermonsAdapter(context)
}