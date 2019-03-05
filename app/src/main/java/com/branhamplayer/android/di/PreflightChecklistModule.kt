package com.branhamplayer.android.di

import com.branhamplayer.android.ui.PreflightChecklistFragment
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides

@Module
class PreflightChecklistModule {

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    fun providePreflightChecklistFragment() = PreflightChecklistFragment()
}
