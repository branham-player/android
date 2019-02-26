package com.branhamplayer.android.di

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.Module
import dagger.Provides

@Module
class RoutingModule(private val context: Context) {

    @Provides
    fun getContext() = context

    @Provides
    fun getSermonsIntent() = Intent(Intent.ACTION_VIEW, Uri.parse("https://branhamplayer.com/sermons"))
}
