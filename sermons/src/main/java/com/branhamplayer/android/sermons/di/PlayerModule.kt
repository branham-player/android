package com.branhamplayer.android.sermons.di

import android.content.Context
import com.branhamplayer.android.sermons.ui.PlayerFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.Module
import dagger.Provides

@Module
class PlayerModule(private val context: Context) {

    @Provides
    fun provideCrossFade() = DrawableTransitionOptions.withCrossFade()

    @Provides
    fun provideGlide() = Glide.with(context)

    @Provides
    fun providePlayerFragment() = PlayerFragment()
}
