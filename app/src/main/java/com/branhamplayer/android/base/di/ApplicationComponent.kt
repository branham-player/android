package com.branhamplayer.android.base.di

import com.branhamplayer.android.di.AuthenticationComponent
import com.branhamplayer.android.di.AuthenticationModule
import com.branhamplayer.android.di.StartupComponent
import com.branhamplayer.android.di.StartupModule
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun newAuthenticationComponent(authenticationModule: AuthenticationModule): AuthenticationComponent
    fun newStartupComponent(startupModule: StartupModule): StartupComponent
}
