package com.branhamplayer.android.di

import com.branhamplayer.android.middleware.StartupMiddleware
import com.branhamplayer.android.reducers.StartupReducer
import com.branhamplayer.android.ui.AuthenticationFragment
import com.branhamplayer.android.ui.MainActivity
import dagger.Component

@Component(modules = [AuthenticationModule::class, RoutingModule::class, StartupModule::class])
interface StartupComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: AuthenticationFragment)
    fun inject(rootMiddleware: StartupMiddleware)
    fun inject(rootReducer: StartupReducer)
}
