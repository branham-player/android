package com.branhamplayer.android.dagger

import com.branhamplayer.android.data.dagger.DatabaseModule
import com.branhamplayer.android.data.dagger.MapperModule
import com.branhamplayer.android.data.dagger.NetworkModule
import com.branhamplayer.android.middleware.StartupMiddleware
import com.branhamplayer.android.reducers.StartupReducer
import com.branhamplayer.android.ui.StartupActivity
import dagger.Component

@Component(
    modules = [
        AuthenticationModule::class,
        DatabaseModule::class,
        MapperModule::class,
        NetworkModule::class,
        PreflightChecklistModule::class,
        RoutingModule::class,
        RxJavaModule::class,
        StartupModule::class
    ]
)
interface StartupComponent {
    fun inject(activity: StartupActivity)
    fun inject(rootMiddleware: StartupMiddleware)
    fun inject(rootReducer: StartupReducer)
}
