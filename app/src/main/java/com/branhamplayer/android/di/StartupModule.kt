package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.ActivityScope
import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Module
import dagger.Provides

@Module
class StartupModule {

    @ActivityScope
    @Provides
    fun getAuthenticationFragment() = AuthenticationFragment()
}
