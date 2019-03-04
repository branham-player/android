package com.branhamplayer.android.di

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.branhamplayer.android.reducers.RoutingReducer
import dagger.Module
import dagger.Provides

@Module
class RoutingModule {

    @Provides
    fun provideRoutingReducer(context: Context, sermonsIntent: Intent) =
        RoutingReducer(context, sermonsIntent)

    @Provides
    fun provideSermonsIntent() = Intent(Intent.ACTION_VIEW, Uri.parse("https://branhamplayer.com/sermons"))
}
