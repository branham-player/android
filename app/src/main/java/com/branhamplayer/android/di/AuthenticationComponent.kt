package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.FragmentScope
import com.branhamplayer.android.base.di.MiddlewareScope
import com.branhamplayer.android.base.di.ReducerScope
import com.branhamplayer.android.base.di.UtilityScope
import com.branhamplayer.android.services.auth0.Auth0Service
import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Component

@FragmentScope
@MiddlewareScope
@ReducerScope
@UtilityScope
@Component(dependencies = [StartupComponent::class], modules = [AuthenticationModule::class])
interface AuthenticationComponent {
    fun inject(fragment: AuthenticationFragment)
    fun inject(utility: Auth0Service)
}
