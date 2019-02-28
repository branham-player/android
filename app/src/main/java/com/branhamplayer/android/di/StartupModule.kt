package com.branhamplayer.android.di

import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Module
import dagger.Provides

@Module
class StartupModule {

    @Provides
    fun getAuthenticationFragment() = AuthenticationFragment()
}
