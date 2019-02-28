package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.ActivityScope
import com.branhamplayer.android.base.di.MiddlewareScope
import com.branhamplayer.android.base.di.ReducerScope
import com.branhamplayer.android.middleware.StartupMiddleware
import com.branhamplayer.android.reducers.StartupReducer
import com.branhamplayer.android.ui.MainActivity
import dagger.Component

@ActivityScope
@MiddlewareScope
@ReducerScope
@Component(modules = [AuthenticationModule::class, StartupModule::class])
interface StartupComponent {
    fun inject(activity: MainActivity)
    fun inject(rootMiddleware: StartupMiddleware)
    fun inject(rootReducer: StartupReducer)
}
