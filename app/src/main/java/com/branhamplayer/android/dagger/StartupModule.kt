package com.branhamplayer.android.dagger

import android.content.Context
import com.branhamplayer.android.ui.StartupActivity
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides

@Module
class StartupModule(private val startupActivity: StartupActivity) {

    @Provides
    fun provideActivity() = startupActivity

    @Provides
    fun provideContext(): Context = startupActivity

    @Provides
    fun provideFirebaseAnalytics(context: Context) = FirebaseAnalytics.getInstance(context)
}
